package com.vvs.demo.project.aspect;

import com.vvs.demo.project.aws.DynamoClient;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ResponseLoggingAspect {
    private final DynamoClient dynamoClient;

/*    @AfterReturning(pointcut = "execution(* com.vvs.demo.project.controller.*.*(..))", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        String path = className + "." + methodName;

        // Extracting status, response body, and error message
        String status = "200"; // Default status, assuming success
        String response = "null";
        String errorMessage = "null";

        // Check if the response object is an instance of ResponseEntity
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            status = String.valueOf(responseEntity.getStatusCode());

            // Extract body from ResponseEntity if present
            Object body = responseEntity.getBody();
            response = Objects.nonNull(body) ? body.toString() : "null";

            // Extract error message if present
            HttpStatus httpStatus = (HttpStatus) responseEntity.getStatusCode();
            errorMessage = responseEntity.hasBody() ? "null" : httpStatus.getReasonPhrase();
        }

        // Saving response data to DynamoDB
        dynamoClient.updateLogEntry(path, status, response, errorMessage);
    }*/
}
