package com.rebote.service;

import com.rebote.domain.DemoData;

import java.util.List;

/**
 * 数据处理接口层
 */
public interface DemoDataService {

    /**
     * 批量插入
     * @param list
     */
    public void insertBatch(List<DemoData> list);

}
