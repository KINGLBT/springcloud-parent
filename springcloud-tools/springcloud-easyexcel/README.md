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

当把Excel中的数据，映射为实体的时候，默认情况下，实体中的属性和Excel表头一一对应，如果实体中的属性顺序
和Excel中不一致的时候，那么EasyExcel在解析数据的时候，可能会报错。

如何去指定实体属性和Excel一一对应？

在实体属性中添加@ExcelProperty注解，通过index或者name去一一对应。
 @ExcelProperty("字符串标题") 或者  @ExcelProperty(index = 2) 这里推荐使用index去匹配。

```
@Data
public class DemoData {

    /**
     * 日期
     */
    @ExcelProperty(index = 1)
    private Date date;

    /**
     * 数字
     */
    @ExcelProperty(index = 2)
    private Double doubleData;

    /**
     * 文本
     */
    @ExcelProperty(index = 0)
    private String text;
}
```

### 读取多个sheet

一个Excel中，可能会有多个sheet，那么如何读取指定的sheet或者全部的sheet?

如下图所示的Excel,两个sheet:
![avatar](https://github.com/KINGLBT/springcloud-parent/blob/master/image/springcloud-tools/springcloud-easyexcel/1-sheet1.png)
![avatar](https://github.com/KINGLBT/springcloud-parent/blob/master/image/springcloud-tools/springcloud-easyexcel/1-sheet2.png)


#### 读取全部的sheet

这种情况适用于，**Excel中所有sheet中的数据格式一致**。

注意：

**监听器中的public void doAfterAllAnalysed(AnalysisContext context);方法，触发的条件是每个
sheet处理完成之后去执行的。此时每次执行完成sheet之后，就需要讲list清空，防止重复处理**


未清空list的代码:

```
@Override
public void doAfterAllAnalysed(AnalysisContext context) {
    // 这里也要保存数据，确保最后遗留的数据也存储到数据库
    saveData();
    log.info("所有数据解析完成！");
}
```

```
@Test
public void readAllSheet() throws FileNotFoundException {
    File file = ResourceUtils.getFile("classpath:demo.xlsx");
    // 批量插入的话，如果想实现事务的话，将下面的代码放入一个事务中
    EasyExcel.read(file, DemoData.class, new DemoDataBatchListener(demoDataService)).doReadAll();
}
```

运行结果：

从图片中发现，数据有重复处理情况，所以，**每处理完成一个sheet，一定要把前一个sheet写入的集合清空，防止重复处理**

![avatar](https://github.com/KINGLBT/springcloud-parent/blob/master/image/springcloud-tools/springcloud-easyexcel/1-重复处理.png)

清空list的代码:

```
@Override
public void doAfterAllAnalysed(AnalysisContext context) {
    // 这里也要保存数据，确保最后遗留的数据也存储到数据库
    saveData();
    log.info("所有数据解析完成！");
    // 存储完成清理 list
    resultList.clear();
}
```

```
@Test
public void readAllSheet() throws FileNotFoundException {
    File file = ResourceUtils.getFile("classpath:demo.xlsx");
    // 批量插入的话，如果想实现事务的话，将下面的代码放入一个事务中
    EasyExcel.read(file, DemoData.class, new DemoDataBatchAllSheetListener(demoDataService)).doReadAll();
}
```

#### 读取部分的sheet

这种情况适用于，**Excel中sheet中的数据格式不一致**、**只读取某几个sheet**



```
@Test
public void partSheet() throws FileNotFoundException {
    File file = ResourceUtils.getFile("classpath:demo.xlsx");
    // 批量插入的话，如果想实现事务的话，将下面的代码放入一个事务中
    ExcelReader excelReader = null;
    try {
        excelReader = EasyExcel.read(file).build();

        // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
        ReadSheet readSheet1 =  EasyExcel.readSheet(0).head(DemoData.class).registerReadListener(new DemoDataBatchListener(demoDataService)).build();
        ReadSheet readSheet2 =  EasyExcel.readSheet(1).head(DemoData.class).registerReadListener(new DemoDataBatchListener(demoDataService)).build();
        // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
        excelReader.read(readSheet1, readSheet2);
    } finally {
        if (excelReader != null) {
            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
            excelReader.finish();
        }
    }
}
```

### 日期、数字或者自定义格式转换

#### 日期格式化

1、将Excel中的日期按照指定格式转化成实体中的字符串

```
@Data
public class ConverterData {

    /**
     * 日期
     */
    @ExcelProperty(index = 1)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private String date;

    /**
     * 数字
     */
    @ExcelProperty(index = 2)
    private Double doubleData;

    /**
     * 文本
     */
    @ExcelProperty(index = 0)
    private String text;
}
```

#### 自定义格式化器

1.自定义格式化器支持哪些Excel数据格式？

CellDataTypeEnum 枚举值中的值，都支持

2.如何使用？

2.1 实现Converter接口

2.2 指定要格式化的Excel格式以及格式化之后的Java数据格式

```
    /**
     * 返回数据在java中的类型
     *
     */
    Class supportJavaTypeKey();

    /**
     * 返回数据在Excel中的数据格式
     *
     */
    CellDataTypeEnum supportExcelTypeKey();
```

2.3 实现格式化方法

```

/**
 * 转化excel对象到java对象
 */
T convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) throws Exception;
```

2.4 代码

```

public class CustomStringStringConverter implements Converter<String> {

    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 这里读的时候会调用,将Excel中的数据格式化成Java中需要的格式
     *
     * @param cellData
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return "自定义：" + cellData.getStringValue();
    }

    /**
     * 这里是写的时候会调用,将Java中的数据格式化成为Excel中想要的格式
     *
     * @param value
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new CellData(value);
    }
}

```

#### 使用自定义格式化器

1、将自定义格式化器使用到整个Excel

如下所示：ConverterData2 对象中，所有的String都会被格式化

```
@Test
    public void converDataWithRegisterConverter() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo2.xlsx");
        // 指定全局的自定义过滤器
        EasyExcel.read(file, ConverterData2.class, new ConverterDataListener(demoDataService)).registerConverter(new CustomStringStringConverter()).sheet().doRead();

    }
```

2、只有Excel中指定的字段需要使用自定义格式转化器

```
@ExcelProperty(index = 0,converter = CustomStringStringConverter.class)
private String text;
```

### 行头设置，标识前几行是表头

在解析Excel的时候，可能会有多行是行头，这种情况下，我们希望不去解析行头，EasyExcel中，可以通过
设置headRowNumber来进行过滤

```
    @Test
    public void complexHeaderRead() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo2.xlsx");
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(file, DemoData.class, new DemoDataListener(demoDataService)).sheet()
                // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                .headRowNumber(1).doRead();
    }
```



### 同步操作返回

### 读取表头数据

```
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
```

### 额外信息（批注、超链接、合并单元格信息读取）

### 数据转换等异常处理

在Excel中，可能存在某写行的数据有问题，这个时候，想跳过不进行处理，可以使用EasyExcle中的public void onException(Exception exception, AnalysisContext context)
进行处理

```
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex());
        }

    }
```


### 不创建对象读

在使用EasyExcle的时候，你可能想偷懒，不想去创建实体类去映射Excel中的数据，如果不指定映射对象的情况下，默认
会把Excel中的每一行数据映射为一个Map<Integer,String>。map中的key为Excel中一行数的列号，value为单元格的值

1、不创建对象读情况下的监视器

```
@Slf4j
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();

    private DemoDataService demoDataService;

    public NoModelDataListener(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        log.info("解析到一条数据:{}", FastJsonUtils.getBeanToJson(data));
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
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
        //log.info("{}条数据，开始存储数据库！", resultList.size());
        demoDataService.insertBatchMap(list);
        //log.info("存储数据库成功！");
    }
}
```

2、代码

```

@Test
    public void noModelRead() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo.xlsx");
        // 这里 只要，然后读取第一个sheet 同步读取会自动finish
        EasyExcel.read(file, new NoModelDataListener(demoDataService)).sheet().doRead();
    }

```

3、不创建对象的情况下，只能使用全局注册自定义格式器


```
/**
     * 不创建对象的读
     */
    @Test
    public void noModelRead() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:demo.xlsx");
        // 这里 只要，然后读取第一个sheet 同步读取会自动finish
        EasyExcel.read(file, new NoModelDataListener(demoDataService)).registerConverter(new CustomStringStringConverter()).sheet().doRead();
    }

```
**使用这种不创建对象解析Excel的方式，在拦截器的使用上有些麻烦**
**使用这种不创建对象解析Excel的方式，持久化到数据库层，处理Map不如处理对象方便快捷**

### web中，上传文件读取Excel


#### 上传controller实现



```
@Controller
@Slf4j
public class EasyExcelController {

    @Autowired
    private DemoDataService demoDataService;


    /**
     * 文件上传
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link UploadData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UploadDataListener}
     * <p>
     * 3. 直接读即可
     */
    @PostMapping("upload")
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), DemoData.class, new DemoDataListener(demoDataService)).sheet().doRead();
        return "success";
    }


}
```