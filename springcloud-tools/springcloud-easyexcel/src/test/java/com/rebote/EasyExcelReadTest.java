package com.rebote;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.rebote.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

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

    @Test
    public void simpleRead() throws FileNotFoundException {
        File file= ResourceUtils.getFile("classpath:demo.xlsx");
        // 同步获取Excel中的全部结果，转化成LinkedHashMap
        List result = EasyExcel.read(file).sheet().doReadSync();
        String jsonResult = FastJsonUtils.getBeanToJson(result);
        log.info(jsonResult);

    }

}
