package aop.springaop.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
/**
 * Spring aop
 * AOP proxy: An object created by the AOP framework in order to implement the aspect
 * contracts (advise method executions and so on). In the Spring Framework,
 * an AOP proxy is a JDK dynamic proxy or a CGLIB proxy.
 */
public class SpringAopAspect {

    private static final String POINT_CUT = "execution(public * aop.springaop.service.SpringAopServiceImpl.doSth())";

    private static final String POINT_CUT_DO_RETURN = "execution(public * aop.springaop.service.SpringAopServiceImpl.doReturn())";


    @Pointcut(POINT_CUT)
    public void pointCut(){}

    @Pointcut(POINT_CUT_DO_RETURN)
    public void pointCutDoReturn(){}

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable  {
        System.out.println("doBefore");
    }

    @After("pointCut()")
    public void doAfter(JoinPoint joinPoint) throws Throwable  {
        System.out.println("doAfter");
    }

    @Around("pointCut()")
    public void doAround(JoinPoint joinPoint) throws Throwable  {
        System.out.println("doAround");
    }

    @AfterReturning("pointCutDoReturn()")
    public void doAfterReturning(JoinPoint joinPoint) throws Throwable  {
        System.out.println("AfterReturning");
    }
}
