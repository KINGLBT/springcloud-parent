package com.rebote.domain;


import com.alibaba.excel.annotation.ExcelIgnore;
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
public class DemoData {

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private Date date;

    /**
     * 数字
     */
    @ExcelProperty(value = "数字")
    private Double doubleData;

    /**
     * 文本
     */
    @ExcelProperty(value = "文本", converter = CustomStringStringConverter.class)
    private String text;

    /**
     * 文本
     */
    @ExcelProperty(value = "文本1", converter = CustomStringStringConverter.class)
    private String text1;

    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignoreColumn;

}
