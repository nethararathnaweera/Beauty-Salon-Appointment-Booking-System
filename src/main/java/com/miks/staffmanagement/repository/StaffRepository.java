package com.miks.staffmanagement.repository;

import com.miks.staffmanagement.model.Staff;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StaffRepository {
    List<Staff> findAll();

    Optional<Staff> findById(UUID id);

    Staff save(Staff staff);

    void deleteById(UUID id);
}

