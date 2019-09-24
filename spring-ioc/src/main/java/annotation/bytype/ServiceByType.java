package annotation.bytype;

import annotation.ComponentInterface;
import annotation.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceByType implements ServiceInterface {

    @Autowired
    private ComponentInterface componentByType;

    @Override
    public void doSth() {
        componentByType.doSth();
    }
}
