package com.rebote;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.rebote.domain.*;
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

    @Test
    public void converData() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo.xlsx");
        // 批量插入的话，如果想实现事务的话，将下面的代码放入一个事务中
        //EasyExcel.read(file, ConverterData.class, new ConverterDataListener(demoDataService)).sheet().doRead();

        // 指定全局的自定义过滤器
        EasyExcel.read(file, ConverterData.class, new ConverterDataListener(demoDataService)).registerConverter(new CustomStringStringConverter()).sheet().doRead();

    }

    @Test
    public void converDataWithRegisterConverter() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo2.xlsx");
        // 指定全局的自定义过滤器
        EasyExcel.read(file, ConverterData2.class, new ConverterDataListener(demoDataService)).registerConverter(new CustomStringStringConverter()).sheet().doRead();

    }

    /**
     * 多行头
     *
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>3. 设置headRowNumber参数，然后读。 这里要注意headRowNumber如果不指定， 会根据你传入的class的{@link ExcelProperty#value()}里面的表头的数量来决定行数，
     * 如果不传入class则默认为1.当然你指定了headRowNumber不管是否传入class都是以你传入的为准。
     */
    @Test
    public void complexHeaderRead() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo2.xlsx");
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(file, DemoData.class, new DemoDataListener(demoDataService)).sheet()
                // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                .headRowNumber(1).doRead();
    }


    /**
     * 数据转换等异常处理
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link ExceptionDemoData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoExceptionListener}
     * <p>
     * 3. 直接读即可
     */
    @Test
    public void exceptionRead() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:error.xlsx");
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(file, DemoData.class, new DemoDataBatchListener(demoDataService)).sheet().doRead();
    }


    /**
     * 不创建对象的读
     */
    @Test
    public void noModelRead() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo.xlsx");
        // 这里 只要，然后读取第一个sheet 同步读取会自动finish
        EasyExcel.read(file, new NoModelDataListener(demoDataService)).registerConverter(new CustomStringStringConverter()).sheet().doRead();
    }


}
