package com.miks.staffmanagement.service;

import com.miks.staffmanagement.exception.StaffNotFoundException;
import com.miks.staffmanagement.model.Staff;
import com.miks.staffmanagement.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public Staff getStaffById(UUID id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with id: " + id));
    }

    @Override
    public Staff addStaff(Staff staff) {
        staff.setId(UUID.randomUUID());
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(UUID id, Staff staff) {
        Staff existing = getStaffById(id);
        existing.setName(staff.getName());
        existing.setRole(staff.getRole());
        existing.setPhone(staff.getPhone());
        existing.setEmail(staff.getEmail());
        return staffRepository.save(existing);
    }

    @Override
    public void removeStaff(UUID id) {
        getStaffById(id);
        staffRepository.deleteById(id);
    }
}

