package aop.springaop.service;

import org.springframework.stereotype.Service;

@Service
public class SpringAopServiceImpl implements SpringAopService {
    @Override
    public void doSth() {
        System.out.println("do sth");
    }

    @Override
    public String doReturn() {
        System.out.println("do nothing");
        return "do nothing";
    }
}
