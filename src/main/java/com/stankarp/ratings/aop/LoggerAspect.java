package com.stankarp.ratings.aop;

import com.stankarp.ratings.message.request.SignUpForm;
import com.stankarp.ratings.message.response.JwtResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Before("execution(* com.stankarp.ratings.controller.*.*(..))")
    public void logBeforeController(JoinPoint joinPoint) {
        logger.info("Method: " + joinPoint.getSignature().toLongString()
                + Arrays.toString(joinPoint.getArgs()));
    }

    @Pointcut("execution(* com.stankarp.ratings.service.UserService.authenticateUser(..))")
    public void authenticateMethodPointcut() {}

    @AfterReturning(pointcut = "authenticateMethodPointcut()", returning = "retVal")
    public void authenticateMethod(JoinPoint joinPoint, Object retVal) {
        logger.info("Method " + joinPoint.getSignature().getName());
        if (retVal != null) {
            JwtResponse response = (JwtResponse)retVal;
            logger.info("User authenticated: " + response.getUsername() + ", authorities: " + response.getAuthorities());
        } else {
            logger.info("User failed to authenticate.");
        }
    }

    @Pointcut("execution(* com.stankarp.ratings.service.UserService.registerUser(..))")
    public void registerMethodPointcut() {}

    @Before("registerMethodPointcut()")
    public void registerMethodPointcut(JoinPoint joinPoint) {
        logger.info("Method " + joinPoint.getSignature().getName());
        SignUpForm form = (SignUpForm)joinPoint.getArgs()[0];
        logger.info("User registered: " + form.getUsername() + ", authorities: " + form.getRole());

    }

    @Pointcut("execution(* com.stankarp.ratings.repository.*.*(..))")
    public void repositoryClassMethods() {}

    @Around("repositoryClassMethods()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        Object retval = pjp.proceed();
        long end = System.nanoTime();
        logger.info("Time of " + pjp.getSignature().toLongString() + " is:" +
                TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        return retval;
    }
}
