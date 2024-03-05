package com.vvs.demo.project.dto.response;

import com.vvs.demo.project.model.Car;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CarResponseDto {
    private Long id;
    private String model;
    private String brand;
    private Car.Type type;
    private Integer inventory;
    private BigDecimal dailyFee;
}
