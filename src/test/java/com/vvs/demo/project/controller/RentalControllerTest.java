package com.vvs.demo.project.controller;

import com.vvs.demo.project.AbstractTestcontainers;
import com.vvs.demo.project.dto.mapper.DtoMapper;
import com.vvs.demo.project.dto.request.RentalRequestDto;
import com.vvs.demo.project.dto.response.RentalResponseDto;
import com.vvs.demo.project.model.Rental;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.service.CarService;
import com.vvs.demo.project.service.RentalService;
import com.vvs.demo.project.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RentalControllerTest extends AbstractTestcontainers {
    private MockMvc mockMvc;

    @Mock
    private RentalService rentalService;

    @Mock
    private CarService carService;

    @Mock
    private DtoMapper<Rental, RentalRequestDto, RentalResponseDto> rentalMapper;

    @Mock
    private UserService userService;

    private RentalController rentalController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        rentalController = new RentalController(rentalMapper, rentalService, carService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(rentalController).build();
    }

    @Test
    public void testGetById() throws Exception {
        Long id = 1L;
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        Rental rental = new Rental();
        RentalResponseDto rentalResponseDto = new RentalResponseDto();

        when(authentication.getName()).thenReturn("username");
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(rentalService.getById(id)).thenReturn(rental);
        when(rentalMapper.toDto(rental)).thenReturn(rentalResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/rentals/{id}", id)
                        .principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(rentalResponseDto.getId()));

        verify(userService, times(1)).findByEmail(anyString());
        verify(rentalService, times(1)).getById(id);
        verify(rentalMapper, times(1)).toDto(rental);
    }

    @Test
    public void testSetActualDate() throws Exception {
        Long id = 1L;
        Rental rental = new Rental();
        RentalResponseDto rentalResponseDto = new RentalResponseDto();

        when(rentalService.getById(id)).thenReturn(rental);
        when(rentalMapper.toDto(rental)).thenReturn(rentalResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/rentals/{id}/return", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(rentalResponseDto.getId()));

        verify(rentalService, times(1)).getById(id);
        verify(rentalService, times(1)).updateActualReturnDate(id);
        verify(rentalMapper, times(1)).toDto(rental);
    }
}
