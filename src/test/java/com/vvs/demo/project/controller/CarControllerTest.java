package com.vvs.demo.project.controller;

import com.vvs.demo.project.AbstractTestcontainers;
import com.vvs.demo.project.model.Car;
import com.vvs.demo.project.service.CarService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarControllerTest extends AbstractTestcontainers {
    private static final Long CAR_ID = 1L;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser(username="admin", roles={"USER","MANAGER"})
    public void testCreateCar() {
        Car car = createCar();

        Mockito.when(carService.createCar(car)).thenReturn(createCar().setId(CAR_ID));
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body(car)
                .when()
                .post("/cars")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("brand", equalTo("Toyota"))
                .body("model", equalTo("Camry"))
                .body("type", equalTo("SEDAN"))
                .body("inventory", equalTo(10))
                .body("dailyFee", equalTo(50.0F));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER","MANAGER"})
    public void testGetAllCars() {
        List<Car> mockCars = List.of(createCar().setId(CAR_ID), createSecondCar());
        Mockito.when(carService.findAll()).thenReturn(mockCars);

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/cars")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(1))
                .body("[0].brand", equalTo("Toyota"))
                .body("[0].model", equalTo("Camry"))
                .body("[0].type", equalTo("SEDAN"))
                .body("[0].inventory", equalTo(10))
                .body("[0].dailyFee", equalTo(50.0F))
                .body("[1].id", equalTo(2))
                .body("[1].brand", equalTo("Honda"))
                .body("[1].model", equalTo("Accord"))
                .body("[1].type", equalTo("SEDAN"))
                .body("[1].inventory", equalTo(5))
                .body("[1].dailyFee", equalTo(60.0F));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER","MANAGER"})
    public void testGetCarById() {
        Long carId = 1L;
        Car car = createCar();
        car.setId(carId);
        Mockito.when(carService.findById(carId)).thenReturn(car);

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/cars/{id}", String.valueOf(carId))
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("brand", equalTo("Toyota"))
                .body("model", equalTo("Camry"))
                .body("type", equalTo("SEDAN"))
                .body("inventory", equalTo(10))
                .body("dailyFee", equalTo(50.00F));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER","MANAGER"})
    public void testDeleteCar() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", CAR_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(carService, Mockito.times(1)).deleteById(CAR_ID);
    }

    private Car createCar() {
        return new Car()
                .setBrand("Toyota")
                .setModel("Camry")
                .setType(Car.Type.SEDAN)
                .setInventory(10)
                .setDailyFee(BigDecimal.valueOf(50.00));
    }

    private Car createSecondCar() {
        return new Car()
                .setId(2L)
                .setBrand("Honda")
                .setModel("Accord")
                .setType(Car.Type.SEDAN)
                .setInventory(5)
                .setDailyFee(BigDecimal.valueOf(60.00));
    }
}
