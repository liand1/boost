package annotation;

import annotation.bytype.ServiceByType;
import annotation.config.AnnotationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Init {

    public void init() {
        // init spring env
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AnnotationConfig.class);


        context.getBean(ServiceByType.class).doSth();
    }

    public static void main(String[] args) {
        new Init().init();
    }
}
