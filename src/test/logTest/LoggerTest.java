package com.imooc.logTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest

public class LoggerTest {

    public static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);//传入的参数为表明哪个类输出的日志

    @Test
    public void test1(){
        String name = "imooc";
        String password = "123";
        logger.info("info...");

    }


}
