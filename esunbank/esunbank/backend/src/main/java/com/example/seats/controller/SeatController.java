package com.example.seats.controller;

import com.example.seats.dto.ChangeSeatRequest;
import com.example.seats.dto.EmployeeDto;
import com.example.seats.dto.SeatDto;
import com.example.seats.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/seats")
    public List<SeatDto> getSeats() {
        return seatService.getSeats();
    }

    @GetMapping("/employees")
    public List<EmployeeDto> getEmployees() {
        return seatService.getEmployees();
    }

    @PostMapping("/seats/change")
    public ResponseEntity<Map<String, String>> changeSeat(@Valid @RequestBody ChangeSeatRequest request) {
        seatService.changeSeat(request.empId(), request.newSeatSeq());
        return ResponseEntity.ok(Map.of("message", "座位調整成功"));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> handleBusinessException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
