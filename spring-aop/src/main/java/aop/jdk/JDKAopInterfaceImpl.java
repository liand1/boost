package aop.jdk;


public class JDKAopInterfaceImpl implements JDKAopInterface{

    @Override
    public void doSth() {
        System.out.println("do sth");
    }
}
