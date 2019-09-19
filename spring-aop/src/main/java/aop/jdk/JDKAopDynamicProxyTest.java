package aop.jdk;

public class JDKAopDynamicProxyTest {

    public static void main(String[] args) {
        // 保存生成的代理类的字节码文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        // 提供一个父类抽象的引用，来指向子类实现
        JDKAopInterface o = new JDKAopDynamicProxy(new JDKAopInterfaceImpl()).getProxy();
        // 如果是子类对象， 则直接会报错，因为在JDK动态代理生成的class文件中，我们可以得知，是先继承了Proxy这个类，
        // 然后再实现父类接口JDKAopInterface， java中我们只允许单继承，但是可以多实现
//        JDKAopInterfaceImpl o = new JDKAopDynamicProxy(new JDKAopInterfaceImpl()).getProxy();

        o.doSth();
    }

}
