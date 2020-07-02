package com.rebote;

import com.alibaba.excel.EasyExcel;
import com.rebote.domain.DemoData;
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

}
