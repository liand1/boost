package annotation.bytype;

import annotation.ComponentInterface;
import org.springframework.stereotype.Component;

@Component
public class ComponentByType implements ComponentInterface {
    @Override
    public void doSth() {
        System.out.println("ComponentByType toSth");
    }

}
