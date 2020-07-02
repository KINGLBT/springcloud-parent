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


**有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去**




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
