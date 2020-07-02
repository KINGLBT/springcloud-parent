package com.rebote.service.impl;

import com.rebote.domain.ConverterData;
import com.rebote.domain.ConverterData2;
import com.rebote.domain.DemoData;
import com.rebote.service.DemoDataService;
import com.rebote.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: 数据处理业务逻辑层
 * @description:
 * @author: luomeng
 * @time: 2020/7/2 14:06
 */
@Service
@Slf4j
public class DemoDataServiceImpl implements DemoDataService {

    @Override
    public void insertBatch(List<DemoData> list) {
        // TODO数据库持久层
        log.info("insertBatch:{}", FastJsonUtils.getBeanToJson(list));
    }

    @Override
    public void insertBatchConverData(List<ConverterData> list) {
        // TODO数据库持久层
        log.info("insertBatchConverData:{}", FastJsonUtils.getBeanToJson(list));
    }

    @Override
    public void insertBatchConverData2(List<ConverterData2> list) {
        // TODO数据库持久层
        log.info("insertBatchConverData:{}", FastJsonUtils.getBeanToJson(list));
    }
}
