package com.rebote;

import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName: BaseTest
 * @description:
 * @author: luomeng
 * @time: 2020/7/2 11:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasyExcelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class BaseTest {
}
