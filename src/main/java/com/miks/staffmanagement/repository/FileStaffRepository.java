package com.miks.staffmanagement.repository;

import com.miks.staffmanagement.model.Staff;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FileStaffRepository implements StaffRepository {

    private static final String DELIMITER = "|";
    private final Path filePath;

    public FileStaffRepository(@Value("${staff.file.path:src/main/resources/data/staff.txt}") String filePath) {
        this.filePath = Paths.get(filePath);
        initializeFile();
    }

    private void initializeFile() {
        try {
            Path parent = filePath.getParent();
            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize staff file", e);
        }
    }

    @Override
    public synchronized List<Staff> findAll() {
        return readAll();
    }

    @Override
    public synchronized Optional<Staff> findById(UUID id) {
        return readAll().stream()
                .filter(staff -> staff.getId().equals(id))
                .findFirst();
    }

    @Override
    public synchronized Staff save(Staff staff) {
        List<Staff> staffList = readAll();
        staffList = staffList.stream()
                .filter(existing -> !existing.getId().equals(staff.getId()))
                .collect(Collectors.toCollection(ArrayList::new));
        staffList.add(staff);
        writeAll(staffList);
        return staff;
    }

    @Override
    public synchronized void deleteById(UUID id) {
        List<Staff> staffList = readAll();
        List<Staff> updated = staffList.stream()
                .filter(staff -> !staff.getId().equals(id))
                .toList();
        writeAll(updated);
    }

    private List<Staff> readAll() {
        try {
            if (Files.notExists(filePath)) {
                return new ArrayList<>();
            }
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<Staff> result = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length != 5) {
                    continue;
                }
                result.add(new Staff(
                        UUID.fromString(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4]
                ));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read staff file", e);
        }
    }

    private void writeAll(List<Staff> staffList) {
        List<String> lines = staffList.stream()
                .map(staff -> String.join(DELIMITER,
                        staff.getId().toString(),
                        sanitize(staff.getName()),
                        sanitize(staff.getRole()),
                        sanitize(staff.getPhone()),
                        sanitize(staff.getEmail())))
                .toList();
        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write staff file", e);
        }
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replace("|", " ").trim();
    }
}

