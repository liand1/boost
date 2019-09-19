package aop.cglib;

public class CglibAopInterfaceImpl implements CglibAopInterface {
    @Override
    public void doSth() {
        System.out.println("do sth");
    }
}
