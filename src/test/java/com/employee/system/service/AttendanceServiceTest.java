```java
package com.employee.system.service;

import com.employee.system.dto.AttendanceDTO;
import com.employee.system.entity.Attendance;
import com.employee.system.entity.Employee;
import com.employee.system.repository.AttendanceRepository;
import com.employee.system.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private Employee employee;
    private Attendance attendance;
    private AttendanceDTO attendanceDTO;
    private LocalDate testDate;
    private LocalDateTime testCheckInTime;
    private LocalDateTime testCheckOutTime;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        attendance = new Attendance();
        attendance.setId(100L);
        attendance.setEmployee(employee);
        testDate = LocalDate.of(2023, 10, 15);
        attendance.setAttendanceDate(testDate);
        attendance.setStatus("PRESENT");
        testCheckInTime = LocalDateTime.of(2023, 10, 15, 9, 0, 0);
        testCheckOutTime = LocalDateTime.of(2023, 10, 15, 17, 30, 0);
        attendance.setCheckInTime(testCheckInTime);
        attendance.setCheckOutTime(testCheckOutTime);
        attendance.setRemarks("On time");
        attendance.setCreatedAt(LocalDateTime.now().minusDays(1));
        attendance.setUpdatedAt(LocalDateTime.now());

        attendanceDTO = new AttendanceDTO();
        attendanceDTO.setId(100L);
        attendanceDTO.setEmployeeId(1L);
        attendanceDTO.setEmployeeName("John Doe");
        attendanceDTO.setAttendanceDate(testDate);
        attendanceDTO.setStatus("PRESENT");
        attendanceDTO.setCheckInTime(testCheckInTime);
        attendanceDTO.setCheckOutTime(testCheckOutTime);
        attendanceDTO.setRemarks("On time");
        attendanceDTO.setCreatedAt(attendance.getCreatedAt());
        attendanceDTO.setUpdatedAt(attendance.getUpdatedAt());
    }

    // markAttendance Tests

    @Test
    @DisplayName("Given valid attendance DTO and existing employee, when marking attendance, then return saved attendance DTO")
    void givenValidAttendanceDTOAndExistingEmployee_whenMarkAttendance_thenReturnSavedAttendanceDTO() {
        // Arrange
        AttendanceDTO inputDTO = new AttendanceDTO();
        inputDTO.setEmployeeId(1L);
        inputDTO.setAttendanceDate(testDate);
        inputDTO.setStatus("PRESENT");
        inputDTO.setCheckInTime(testCheckInTime);
        inputDTO.setCheckOutTime(testCheckOutTime);
        inputDTO.setRemarks("On time");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> {
            Attendance att = invocation