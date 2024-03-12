package com.vvs.demo.project.aspect;

import com.vvs.demo.project.aws.DynamoClient;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class RequestLoggingAspect {
    private final DynamoClient dynamoClient;

    @AfterReturning(pointcut = "execution(* com.vvs.demo.project.controller.*.*(..))", returning = "result")
    public void logControllerCall(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        String path = className + "." + methodName;

        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            params.append(arg.toString()).append(", ");
        }
        String parameters = params.toString();

        String body = result != null ? result.toString() : null;

        String method = joinPoint.getSignature().toShortString().split(" ")[0];

        String timestamp = LocalDateTime.now().toString();

        dynamoClient.saveData(path, method, parameters, body, timestamp);
    }

/*    @AfterReturning(pointcut = "execution(* com.vvs.demo.project.controller.*.*(..))", returning = "result")
    public void logRequest(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        String path = className + "." + methodName;

        // Extracting method and parameters
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        String parameters = Arrays.toString(args);

        // Extracting body if it exists
        String body = null;
        for (Object arg : args) {
            if (arg != null && arg.getClass().getSimpleName().equalsIgnoreCase("RequestEntity")) {
                body = arg.toString();
                break;
            }
        }

        String timestamp = LocalDateTime.now().toString();

        // Saving data to DynamoDB
        dynamoClient.saveData(path, method, parameters, body, timestamp);
    }*/
}
