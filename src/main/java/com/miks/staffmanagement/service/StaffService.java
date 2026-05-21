package com.miks.staffmanagement.service;

import com.miks.staffmanagement.model.Staff;

import java.util.List;
import java.util.UUID;

public interface StaffService {
    List<Staff> getAllStaff();

    Staff getStaffById(UUID id);

    Staff addStaff(Staff staff);

    Staff updateStaff(UUID id, Staff staff);

    void removeStaff(UUID id);
}

