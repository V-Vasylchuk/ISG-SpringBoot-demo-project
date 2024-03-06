package com.vvs.demo.project.service.impl;

import com.vvs.demo.project.model.Car;
import com.vvs.demo.project.model.Rental;
import com.vvs.demo.project.model.User;
import com.vvs.demo.project.repository.RentalRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RentalServiceImplTest {
    private static final Long ID = 1L;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testSave() {
        Rental rental = createRental();
        when(rentalRepository.save(rental)).thenReturn(rental);

        Rental savedRental = rentalService.save(rental);

        assertNotNull(savedRental);
        assertEquals(rental, savedRental);
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    void testGetById() {
        Long id = ID;
        Rental rental = createRental();
        when(rentalRepository.getReferenceById(id)).thenReturn(rental);

        Rental retrievedRental = rentalService.getById(id);

        assertNotNull(retrievedRental);
        assertEquals(rental, retrievedRental);
        verify(rentalRepository, times(1)).getReferenceById(id);
    }

    @Test
    void testDelete() {
        Long id = ID;
        doNothing().when(rentalRepository).deleteById(id);

        assertDoesNotThrow(() -> rentalService.delete(id));
        verify(rentalRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindAllByUserId() {
        Long userId = ID;
        List<Rental> rentals = Collections.singletonList(createRental());
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(rentalRepository.findAllByUserId(userId, pageRequest)).thenReturn(rentals);

        List<Rental> retrievedRentals = rentalService.findAllByUserId(userId, pageRequest);

        assertNotNull(retrievedRentals);
        assertEquals(rentals, retrievedRentals);
        verify(rentalRepository, times(1)).findAllByUserId(userId, pageRequest);
    }

    @Test
    void testUpdateActualReturnDate_RentalWithNullActualReturnDate() {
        Long rentalId = ID;
        Rental rental = createRental();
        rental.setActualReturnDate(null);
        when(rentalRepository.getReferenceById(rentalId)).thenReturn(rental);
        when(rentalRepository.save(rental)).thenReturn(rental);

        assertDoesNotThrow(() -> rentalService.updateActualReturnDate(rentalId));
        assertNotNull(rental.getActualReturnDate());
        verify(rentalRepository, times(1)).getReferenceById(rentalId);
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    void testUpdateActualReturnDate_RentalWithNonNullActualReturnDate() {
        Long rentalId = ID;
        Rental rental = createRental();
        rental.setActualReturnDate(LocalDateTime.now());
        when(rentalRepository.getReferenceById(rentalId)).thenReturn(rental);

        assertThrows(RuntimeException.class, () -> rentalService.updateActualReturnDate(rentalId));
        verify(rentalRepository, times(1)).getReferenceById(rentalId);
    }

    @Test
    void testFindAllByActualReturnDateAfterReturnDate_ReturnsListOfRentals() {
        List<Rental> rentals = Collections.singletonList(createRental());
        when(rentalRepository.findAllByActualReturnDateAfterReturnDate()).thenReturn(rentals);

        List<Rental> retrievedRentals = rentalService.findAllByActualReturnDateAfterReturnDate();

        assertNotNull(retrievedRentals);
        assertEquals(rentals, retrievedRentals);
        verify(rentalRepository, times(1)).findAllByActualReturnDateAfterReturnDate();
    }

    private Rental createRental() {
        LocalDateTime rentalDate = LocalDateTime.of(2023, 6, 1, 10, 0);
        LocalDateTime returnDate = LocalDateTime.of(2023, 6, 5, 12, 0);
        LocalDateTime actualReturnDate = LocalDateTime.of(2023, 6, 5, 14, 0);
        Car car = createCar();
        User user = createUser();
        return new Rental()
                .setRentalDate(rentalDate)
                .setReturnDate(returnDate)
                .setActualReturnDate(actualReturnDate)
                .setCar(car)
                .setUser(user);
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

    private User createUser() {
        return new User()
                .setId(ID)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setPassword("password")
                .setRole(User.Role.CUSTOMER);
    }
}