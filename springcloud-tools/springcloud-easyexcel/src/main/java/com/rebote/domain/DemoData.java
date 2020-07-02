package com.rebote.domain;


import com.alibaba.excel.annotation.ExcelProperty;
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
    @ExcelProperty(index = 1)
    private Date date;

    /**
     * 数字
     */
    @ExcelProperty(index = 2)
    private Double doubleData;

    /**
     * 文本
     */
    @ExcelProperty(index = 0)
    private String text;
}
