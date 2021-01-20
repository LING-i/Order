package com.imooc.logTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j   //直接log.操作就可
public class LoggerTest2 {

    @Test
    public void test1(){
        log.info("info.......");
        log.error("error.......");
    }


}
