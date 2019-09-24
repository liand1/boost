package annotation.byname;

import annotation.ComponentInterface;
import org.springframework.stereotype.Component;

@Component
public class ComponentByName implements ComponentInterface {
    @Override
    public void doSth() {
        System.out.println("ComponentByName toSth");
    }
}
