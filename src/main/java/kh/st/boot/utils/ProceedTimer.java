package kh.st.boot.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
public class ProceedTimer {

    //페이지에는 안된다 나와있는데 다오도 동작함 ("execution(* kh.st.boot.dao.원하는DAO..*(..))")
    @Pointcut("execution(* kh.st.boot.service..*(..))")
    private void timer(){};

    @Around("timer()")
    public Object Timer(ProceedingJoinPoint joinPoint) throws Throwable {
        long st = System.currentTimeMillis(); // 시작시간
        Object proceed = joinPoint.proceed(); // 메서드 실행
        long et = System.currentTimeMillis(); // 끝나는 시간
       // System.out.println(joinPoint.getSignature() + " :: Timer ::  " + (et - st) + "ms ::");//콘솔창이 씨끄러우면 여기 주석
        return proceed;//null이면 오류
    }
}

