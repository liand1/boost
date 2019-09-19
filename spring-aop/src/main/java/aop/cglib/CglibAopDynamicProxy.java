package aop.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibAopDynamicProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.getName().equals("doSth")) {
            System.out.println("###########ready go##############");
        }
        Object rev = methodProxy.invokeSuper(o, objects);
        if (method.getName().equals("doSth")) {
            System.out.println("###########after##############");
        }
        return rev;
    }
}
