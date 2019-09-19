package aop.springaop.service;

import aop.SpringAopApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringAopApplication.class})
public class SpringAopServiceImplTest {

    @Autowired
    private SpringAopService springAopService;

    @Test
    public void doSth() {
        springAopService.doSth();
    }

    @Test
    public void doReturn() {
        springAopService.doReturn();
    }
}