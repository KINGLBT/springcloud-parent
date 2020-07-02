# EasyExcel学习笔记

EasyExcel大数据量读操作、写操作学习

## EasyExcel读

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### 最简单的读

#### 如下excel示例

![avatar](https://github.com/KINGLBT/springcloud-parent/blob/master/image/springcloud-tools/springcloud-easyexcel/1-excel.png)

#### Excel表格数据实体映射

```
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
```

#### 监听器

1、监听器的工作，主要是对Excel数据进行解析，里面常用的方法有以下两个：
```
// 每解析完一行数据会触发发一次
public void invoke(DemoData data, AnalysisContext context);
// 解析完所有的数据会去触发一次
public void doAfterAllAnalysed(AnalysisContext context);
```

2、监听器获取所有的数据集合
```
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
```

3、实现全部数据入库

可以在invoke方法中，每解析完一条数据，就调用一次入库，但是这种方式，对数据库资源消耗比较大，
每插入一次，都需要获取一次连接

可以在doAfterAllAnalysed方法中，所有数据解析完成之后，一批次全部插入

**需要注意的是，监听器不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去**

```
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
}
```

4、批量实现全部数据入库
在数据量特别大的情况下，一次性把数据全部放入List中，对服务器内存，以及数据库都是有很大的要求。因此，在数据量大的情况下，
上面的那种方式，就行不通了。
```
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
```

#### 代码
```
@Slf4j
public class EasyExcelReadTest extends BaseTest {

    @Resource
    private DemoDataService demoDataService;

    @Test
    public void simpleReadAndSave() throws FileNotFoundException {
        File file= ResourceUtils.getFile("classpath:demo.xlsx");
        // 同步获取Excel中的全部结果，转化成LinkedHashMap
        EasyExcel.read(file, DemoData.class,new DemoDataBatchListener(demoDataService)).sheet().doRead();
    }

}
```

### index以及name使用

A step by step series of examples that tell you have to get a development env running

Say what the step will be

```
Give the example
```

### 读取多个sheet

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo


### 日期、数字或者自定义格式转换

### 行头设置，标识前几行是表头

### 同步操作返回

### 读取表头数据

### 额外信息（批注、超链接、合并单元格信息读取）

### 数据转换等异常处理

### 不创建对象读

### web中，上传文件读取
