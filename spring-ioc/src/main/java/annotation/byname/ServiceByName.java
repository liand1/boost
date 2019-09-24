package annotation.byname;

import annotation.ComponentInterface;
import annotation.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServiceByName implements ServiceInterface {

    @Autowired
    private ComponentInterface componentByName;

    @Override
    public void doSth() {
        componentByName.doSth();
    }
}
