package com.rebote.domain;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: DemoData
 * @description:
 * @author: luomeng
 * @time: 2020/7/1 11:35
 */
@Data
public class ConverterData {

    /**
     * 日期
     */
    @ExcelProperty(index = 1)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private String date;

    /**
     * 数字
     */
    @ExcelProperty(index = 2)
    private Double doubleData;

    /**
     * 文本
     */
    @ExcelProperty(index = 0,converter = CustomStringStringConverter.class)
    private String text;
}
