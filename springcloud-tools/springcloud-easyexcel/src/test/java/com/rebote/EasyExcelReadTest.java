package com.rebote;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.rebote.domain.DemoData;
import com.rebote.domain.DemoDataBatchAllSheetListener;
import com.rebote.domain.DemoDataBatchListener;
import com.rebote.service.DemoDataService;
import com.rebote.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @ClassName: EasyExcelRead
 * @description:
 * @author: luomeng
 * @time: 2020/7/2 11:42
 */
@Slf4j
public class EasyExcelReadTest extends BaseTest {

    @Resource
    private DemoDataService demoDataService;

    @Test
    public void simpleRead() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo.xlsx");
        // 同步获取Excel中的全部结果，转化成LinkedHashMap
        List result = EasyExcel.read(file).sheet().doReadSync();
        String jsonResult = FastJsonUtils.getBeanToJson(result);
        log.info(jsonResult);

    }

    @Test
    public void simpleReadAndSave() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo.xlsx");
        // 批量插入的话，如果想实现事务的话，将下面的代码放入一个事务中
        EasyExcel.read(file, DemoData.class, new DemoDataBatchListener(demoDataService)).sheet().doRead();
    }

    @Test
    public void readAllSheet() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo.xlsx");
        // 批量插入的话，如果想实现事务的话，将下面的代码放入一个事务中
        EasyExcel.read(file, DemoData.class, new DemoDataBatchListener(demoDataService)).doReadAll();
    }

    @Test
    public void partSheet() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo.xlsx");
        // 批量插入的话，如果想实现事务的话，将下面的代码放入一个事务中
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(file).build();

            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 =
                    EasyExcel.readSheet(0).head(DemoData.class).registerReadListener(new DemoDataBatchListener(demoDataService)).build();
            ReadSheet readSheet2 =
                    EasyExcel.readSheet(1).head(DemoData.class).registerReadListener(new DemoDataBatchListener(demoDataService)).build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }


}
