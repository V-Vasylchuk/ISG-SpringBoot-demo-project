package com.vvs.demo.project.service;

import com.vvs.demo.project.model.Rental;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface RentalService {
    Rental save(Rental rental);

    Rental getById(Long id);

    void delete(Long id);

    List<Rental> findAllByUserId(Long userId, PageRequest pageRequest);

    void updateActualReturnDate(Long id);

    List<Rental> findAllByActualReturnDateAfterReturnDate();
}
