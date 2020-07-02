package com.rebote.domain;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.rebote.service.DemoDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DemoDataListener
 * @description:
 * @author: luomeng
 * @time: 2020/7/1 11:42
 */
public class ConverterDataListener extends AnalysisEventListener<ConverterData2> {

    List<ConverterData2> resultList = new ArrayList<>();

    private DemoDataService demoDataService;

    public ConverterDataListener(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @Override
    public void invoke(ConverterData2 data, AnalysisContext context) {
        // 每解析完一行数据之后，都会调用该方法
        resultList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析完所有的数据之后，会调用该方法
        demoDataService.insertBatchConverData2(resultList);
    }
}
