package com.rebote;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.rebote.domain.DemoData;
import com.rebote.service.DemoDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName: EasyExcelRead
 * @description:
 * @author: luomeng
 * @time: 2020/7/2 11:42
 */
@Slf4j
public class EasyExcelWriteTest extends BaseTest {

    @Resource
    private DemoDataService demoDataService;

    private List list = new ArrayList();


    @Before
    public void initData() {
        for (int i = 0; i <= 100; i++) {
            DemoData demoData = new DemoData();
            demoData.setDate(new Date());
            demoData.setDoubleData(new Double(i));
            demoData.setText("标题" + i);
            demoData.setText1("标题" + i);
            demoData.setIgnoreColumn("我是忽略的字段" + i);
            list.add(demoData);
        }
    }

    /**
     * 最简单的写
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 直接写即可
     */
    @Test
    public void simpleWrite() {
        // 写法1
        String fileName = System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(list);

    }


    /**
     * 根据参数只导出指定列
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 根据自己或者排除自己需要的列
     * <p>
     * 3. 直接写即可
     *
     * @since 2.1.1
     */
    @Test
    public void excludeWrite() {
        String fileName = System.currentTimeMillis() + ".xlsx";

        // 根据用户传入字段 假设我们要忽略 date
        Set<String> excludeColumnFiledNames = new HashSet<String>();
        excludeColumnFiledNames.add("date");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).excludeColumnFiledNames(excludeColumnFiledNames).sheet("模板")
                .doWrite(list);

    }


    /**
     * 根据参数只导出指定列
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 根据自己或者排除自己需要的列
     * <p>
     * 3. 直接写即可
     *
     * @since 2.1.1
     */
    @Test
    public void includeWrite() {
        String fileName = System.currentTimeMillis() + ".xlsx";

        // 根据用户传入字段 假设我们只要导出 date
        Set<String> includeColumnFiledNames = new HashSet<String>();
        includeColumnFiledNames.add("date");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).includeColumnFiledNames(includeColumnFiledNames).sheet("模板")
                .doWrite(list);
    }


    /**
     * 重复多次写入
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link ComplexHeadData}
     * <p>
     * 2. 使用{@link ExcelProperty}注解指定复杂的头
     * <p>
     * 3. 直接调用二次写入即可
     */
    @Test
    public void repeatedWrite() {
        // 方法1 如果写到同一个sheet
        String fileName = System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = null;
        try {
            // 这里 需要指定写用哪个class去写
            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
            for (int i = 0; i < 5; i++) {
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = list;
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

    }


    /**
     * 动态头，实时生成头写入
     * <p>
     * 思路是这样子的，先创建List<String>头格式的sheet仅仅写入头,然后通过table 不写入头的方式 去写入数据
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 然后写入table即可
     */
    @Test
    public void dynamicHeadWrite() {
        String fileName = System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName)
                // 这里放入动态头
                .head(head()).sheet("模板")
                // 当然这里数据也可以用 List<List<String>> 去传入
                .doWrite(list);
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = new ArrayList<String>();
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = new ArrayList<String>();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

}
