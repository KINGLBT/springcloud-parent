package com.rebote.domain;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.rebote.service.DemoDataService;
import com.rebote.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DemoDataListener
 * @description:
 * @author: luomeng
 * @time: 2020/7/1 11:42
 */
@Slf4j
public class DemoDataBatchListener extends AnalysisEventListener<DemoData> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    /**
     * 结果集合
     */
    List<DemoData> resultList = new ArrayList<>();

    private DemoDataService demoDataService;

    public DemoDataBatchListener(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        // 每解析完一行数据之后，都会调用该方法
        log.info("解析到一条数据:{}", FastJsonUtils.getBeanToJson(data));
        resultList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (resultList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            resultList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", resultList.size());
        demoDataService.insertBatch(resultList);
        log.info("存储数据库成功！");
    }
}
