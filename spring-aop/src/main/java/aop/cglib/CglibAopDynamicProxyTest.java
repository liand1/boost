package aop.cglib;

import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;

public class CglibAopDynamicProxyTest {

    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "com/sun/proxy");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CglibAopInterfaceImpl.class);
        enhancer.setCallback(new CglibAopDynamicProxy());
        CglibAopInterface o = (CglibAopInterface)enhancer.create();

        o.doSth();
    }
}
