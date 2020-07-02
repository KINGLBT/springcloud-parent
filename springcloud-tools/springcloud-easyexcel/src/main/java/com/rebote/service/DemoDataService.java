package com.rebote.service;

import com.rebote.domain.ConverterData;
import com.rebote.domain.ConverterData2;
import com.rebote.domain.DemoData;

import java.util.List;
import java.util.Map;

/**
 * 数据处理接口层
 */
public interface DemoDataService {

    /**
     * 批量插入
     *
     * @param list
     */
    public void insertBatch(List<DemoData> list);


    /**
     * 批量插入
     *
     * @param list
     */
    public void insertBatchConverData(List<ConverterData> list);

    /**
     * 批量插入
     *
     * @param list
     */
    public void insertBatchConverData2(List<ConverterData2> list);


    /**
     * 批量插入
     *
     * @param list
     */
    public void insertBatchMap(List<Map<Integer,String>> list);

}
