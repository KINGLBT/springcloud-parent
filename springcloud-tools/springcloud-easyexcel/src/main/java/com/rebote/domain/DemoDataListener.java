package com.rebote.domain;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DemoDataListener
 * @description:
 * @author: luomeng
 * @time: 2020/7/1 11:42
 */
public class DemoDataListener extends AnalysisEventListener<DemoData> {

    List<DemoData> result = new ArrayList<>();

    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        // 每解析完一行数据之后，都会调用该方法
        result.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析完所有的数据之后，会调用该方法

    }
}
