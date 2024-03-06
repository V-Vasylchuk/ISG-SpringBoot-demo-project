package com.vvs.demo.project.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CarAspect {

    @After("execution(* com.vvs.demo.project.service.CarService.createCar(..))")
    public void logNewRentalCreate() {
        log.info("New car was added.");
    }
}
