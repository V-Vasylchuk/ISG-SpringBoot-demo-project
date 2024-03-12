package com.vvs.demo.project.service.impl;

import com.vvs.demo.project.model.Car;
import com.vvs.demo.project.repository.CarRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CarServiceImplTest {
    private static final Long ID = 1L;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    public void testCreateCar() {
        Car car = createCar();
        when(carRepository.save(car)).thenReturn(car);
        Car result = carService.createCar(car);
        assertEquals(car, result);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    public void testFindAll() {
        List<Car> cars = Arrays.asList(createCar(), createSecondCar());
        when(carRepository.findAll()).thenReturn(cars);
        List<Car> result = carService.findAll();
        assertEquals(cars, result);
        verify(carRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Long carId = ID;
        Car car = createCar();
        when(carRepository.getReferenceById(carId)).thenReturn(car);
        Car result = carService.findById(carId);
        assertEquals(car, result);
        verify(carRepository, times(1)).getReferenceById(carId);
    }

    @Test
    public void testUpdate() {
        Long carId = ID;
        Car car = createCar();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(car)).thenReturn(car);
        Car result = carService.update(car);
        assertEquals(car, result);
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).saveAndFlush(car);
    }

    @Test
    public void testDeleteById() {
        Long carId = ID;
        carService.deleteById(carId);
        verify(carRepository, times(1)).deleteById(carId);
    }

    private Car createCar() {
        return new Car()
                .setId(ID)
                .setBrand("Jaguar")
                .setModel("S-Type")
                .setType(Car.Type.SEDAN)
                .setInventory(10)
                .setDailyFee(BigDecimal.valueOf(50));
    }

    private Car createSecondCar() {
        return new Car()
                .setId(2L)
                .setBrand("Honda")
                .setModel("Accord")
                .setType(Car.Type.SEDAN)
                .setInventory(8)
                .setDailyFee(BigDecimal.valueOf(60));
    }
}