package com.vvs.demo.project.dto.mapper;

import com.vvs.demo.project.dto.request.RentalRequestDto;
import com.vvs.demo.project.dto.response.RentalResponseDto;
import com.vvs.demo.project.model.Rental;
import com.vvs.demo.project.service.CarService;
import com.vvs.demo.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RentalMapper implements DtoMapper<Rental, RentalRequestDto, RentalResponseDto> {
    private final CarService carService;
    private final UserService userService;

    @Override
    public Rental toModel(RentalRequestDto requestDto) {
        return new Rental()
                .setRentalDate(requestDto.getRentalDate())
                .setReturnDate(requestDto.getReturnDate())
                .setActualReturnDate(requestDto.getActualReturnDate())
                .setUser(userService.findById(requestDto.getUserId()))
                .setCar(carService.findById(requestDto.getCarId()));
    }

    @Override
    public RentalResponseDto toDto(Rental model) {
        return new RentalResponseDto()
                .setId(model.getId())
                .setRentalDate(model.getRentalDate())
                .setReturnDate(model.getReturnDate())
                .setActualReturnDate(model.getActualReturnDate())
                .setUserId(model.getUser().getId())
                .setCarId(model.getCar().getId());
    }
}
