package com.vvs.demo.project.controller;

import com.vvs.demo.project.dto.mapper.DtoMapper;
import com.vvs.demo.project.dto.request.RentalRequestDto;
import com.vvs.demo.project.dto.response.RentalResponseDto;
import com.vvs.demo.project.model.Rental;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.service.CarService;
import com.vvs.demo.project.service.RentalService;
import com.vvs.demo.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {
    private final DtoMapper<Rental, RentalRequestDto, RentalResponseDto> rentalMapper;
    private final RentalService rentalService;
    private final CarService carService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Add a new rental")
    public RentalResponseDto add(@RequestBody @Valid RentalRequestDto rentalRequestDto) {
        RentalResponseDto rentalResponseDto = rentalMapper
                .toDto(rentalService
                        .save(rentalMapper
                                .toModel(rentalRequestDto)));
        carService.decreaseInventory(rentalResponseDto.getCarId(), 1); // One car for testing
        return rentalResponseDto;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by id")
    public RentalResponseDto getById(Authentication authentication,
                                     @Parameter(description = "Rental's id to find it")
                                     @PathVariable Long id) {
        User user = userService.findByEmail(authentication.getName()).get();
        if (user.getRole() == User.Role.CUSTOMER && !Objects.equals(user.getId(),
                rentalService.getById(id).getUser().getId())) {
            throw new RuntimeException("Can't get by this id: " + id);
        }
        return rentalMapper.toDto(rentalService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all rentals by userId and status")
    public List<RentalResponseDto> getByUserAndActive(
            @RequestParam("user_id")
            @Parameter(description = "user id") Long userId,
            @RequestParam("is_active")
            @Parameter(description = "active rental or not") boolean isActive,
            @RequestParam(defaultValue = "10")
            @Parameter(description = "default value is '10'") Integer count,
            @RequestParam(defaultValue = "0")
            @Parameter(description = "default value is '0'") Integer page) {

        PageRequest pageRequest = PageRequest.of(page, count);

        if (isActive) {
            return rentalService.findAllByUserId(userId, pageRequest)
                    .stream()
                    .map(rentalMapper::toDto)
                    .filter(d -> d.getActualReturnDate() == null)
                    .collect(Collectors.toList());
        }
        return rentalService.findAllByUserId(userId, pageRequest)
                .stream()
                .map(rentalMapper::toDto)
                .filter(d -> d.getActualReturnDate() != null)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Set car return date")
    public RentalResponseDto setActualDate(
            @Parameter(description = "Rental's id to find set the date of car return")
            @PathVariable Long id) {
        rentalService.updateActualReturnDate(id);
        RentalResponseDto rentalResponseDto = rentalMapper.toDto(rentalService.getById(id));
        carService.increaseInventory(rentalResponseDto.getCarId(), 1);
        return rentalResponseDto;
    }
}
