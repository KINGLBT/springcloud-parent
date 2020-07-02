package com.rebote.domain;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.rebote.service.DemoDataService;
import com.rebote.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DemoDataListener
 * @description:
 * @author: luomeng
 * @time: 2020/7/1 11:42
 */
@Slf4j
public class DemoDataListener extends AnalysisEventListener<DemoData> {

    List<DemoData> resultList = new ArrayList<>();

    private DemoDataService demoDataService;

    public DemoDataListener(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        // 每解析完一行数据之后，都会调用该方法
        resultList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析完所有的数据之后，会调用该方法
        demoDataService.insertBatch(resultList);
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        // 解析表头的时候，会调用该方法
        log.info("解析到一条头数据:{}", FastJsonUtils.getBeanToJson(headMap));
    }
}
