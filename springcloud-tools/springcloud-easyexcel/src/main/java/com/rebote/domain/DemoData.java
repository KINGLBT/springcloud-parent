package com.rebote.domain;


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
     * 文本
     */
    private String text;

    /**
     * 日期
     */
    private Date date;

    /**
     * 数字
     */
    private Double doubleData;

}
