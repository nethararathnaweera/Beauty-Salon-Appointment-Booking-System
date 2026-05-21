package com.miks.staffmanagement.service;

import com.miks.staffmanagement.exception.StaffNotFoundException;
import com.miks.staffmanagement.model.Staff;
import com.miks.staffmanagement.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceImplTest {

    @Mock
    private StaffRepository repository;

    @InjectMocks
    private StaffServiceImpl service;

    private Staff sample;

    @BeforeEach
    void setUp() {
        sample = new Staff(UUID.randomUUID(), "Alice", "Lecturer", "+94771234567", "alice@uni.edu");
    }

    @Test
    void shouldReturnAllStaff() {
        when(repository.findAll()).thenReturn(List.of(sample));

        List<Staff> result = service.getAllStaff();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void shouldReturnStaffById() {
        when(repository.findById(sample.getId())).thenReturn(Optional.of(sample));

        Staff result = service.getStaffById(sample.getId());

        assertEquals(sample.getName(), result.getName());
        verify(repository).findById(sample.getId());
    }

    @Test
    void shouldThrowWhenStaffNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class, () -> service.getStaffById(id));
    }

    @Test
    void shouldAddStaffWithGeneratedId() {
        Staff request = new Staff(null, "Bob", "Admin", "+94779876543", "bob@uni.edu");
        when(repository.save(any(Staff.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Staff created = service.addStaff(request);

        assertNotNull(created.getId());
        assertEquals("Bob", created.getName());
        verify(repository).save(any(Staff.class));
    }

    @Test
    void shouldUpdateStaff() {
        Staff request = new Staff(null, "Alice Updated", "Head Lecturer", "+94770001122", "alice.updated@uni.edu");
        when(repository.findById(sample.getId())).thenReturn(Optional.of(sample));
        when(repository.save(any(Staff.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Staff updated = service.updateStaff(sample.getId(), request);

        assertEquals("Alice Updated", updated.getName());
        assertEquals("Head Lecturer", updated.getRole());
        verify(repository).save(sample);
    }

    @Test
    void shouldRemoveStaff() {
        when(repository.findById(sample.getId())).thenReturn(Optional.of(sample));

        service.removeStaff(sample.getId());

        verify(repository).deleteById(sample.getId());
    }
}

