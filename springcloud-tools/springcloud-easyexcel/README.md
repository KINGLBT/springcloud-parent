# EasyExcel学习笔记

* [EasyExcel学习笔记](#easyexcel%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0)
  * [EasyExcel读](#easyexcel%E8%AF%BB)
    * [最简单的读](#%E6%9C%80%E7%AE%80%E5%8D%95%E7%9A%84%E8%AF%BB)
      * [如下excel示例](#%E5%A6%82%E4%B8%8Bexcel%E7%A4%BA%E4%BE%8B)
      * [Excel表格数据实体映射](#excel%E8%A1%A8%E6%A0%BC%E6%95%B0%E6%8D%AE%E5%AE%9E%E4%BD%93%E6%98%A0%E5%B0%84)
      * [监听器](#%E7%9B%91%E5%90%AC%E5%99%A8)
      * [代码](#%E4%BB%A3%E7%A0%81)
    * [index以及name使用](#index%E4%BB%A5%E5%8F%8Aname%E4%BD%BF%E7%94%A8)
    * [读取多个sheet](#%E8%AF%BB%E5%8F%96%E5%A4%9A%E4%B8%AAsheet)
    * [日期、数字或者自定义格式转换](#%E6%97%A5%E6%9C%9F%E6%95%B0%E5%AD%97%E6%88%96%E8%80%85%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A0%BC%E5%BC%8F%E8%BD%AC%E6%8D%A2)
    * [行头设置，标识前几行是表头](#%E8%A1%8C%E5%A4%B4%E8%AE%BE%E7%BD%AE%E6%A0%87%E8%AF%86%E5%89%8D%E5%87%A0%E8%A1%8C%E6%98%AF%E8%A1%A8%E5%A4%B4)
    * [同步操作返回](#%E5%90%8C%E6%AD%A5%E6%93%8D%E4%BD%9C%E8%BF%94%E5%9B%9E)
    * [读取表头数据](#%E8%AF%BB%E5%8F%96%E8%A1%A8%E5%A4%B4%E6%95%B0%E6%8D%AE)
    * [额外信息（批注、超链接、合并单元格信息读取）](#%E9%A2%9D%E5%A4%96%E4%BF%A1%E6%81%AF%E6%89%B9%E6%B3%A8%E8%B6%85%E9%93%BE%E6%8E%A5%E5%90%88%E5%B9%B6%E5%8D%95%E5%85%83%E6%A0%BC%E4%BF%A1%E6%81%AF%E8%AF%BB%E5%8F%96)
    * [数据转换等异常处理](#%E6%95%B0%E6%8D%AE%E8%BD%AC%E6%8D%A2%E7%AD%89%E5%BC%82%E5%B8%B8%E5%A4%84%E7%90%86)
    * [不创建对象读](#%E4%B8%8D%E5%88%9B%E5%BB%BA%E5%AF%B9%E8%B1%A1%E8%AF%BB)
    * [web中，上传文件读取](#web%E4%B8%AD%E4%B8%8A%E4%BC%A0%E6%96%87%E4%BB%B6%E8%AF%BB%E5%8F%96)

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
上面的那种方式，就行不通了,可以采用批量入库的方式，如下：

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

当把Excel中的数据，映射为实体的时候，


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
