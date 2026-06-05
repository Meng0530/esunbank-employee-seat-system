package com.example.seats.service;

import com.example.seats.dto.EmployeeDto;
import com.example.seats.dto.SeatDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeatService {
    private final JdbcTemplate jdbcTemplate;

    public SeatService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SeatDto> getSeats() {
        String sql = """
                SELECT S.FLOOR_SEAT_SEQ, S.FLOOR_NO, S.SEAT_NO, E.EMP_ID
                  FROM SEATING_CHART S
                  LEFT JOIN EMPLOYEE E ON S.FLOOR_SEAT_SEQ = E.FLOOR_SEAT_SEQ
                 ORDER BY S.FLOOR_NO, S.SEAT_NO
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new SeatDto(
                rs.getInt("FLOOR_SEAT_SEQ"),
                rs.getInt("FLOOR_NO"),
                rs.getInt("SEAT_NO"),
                rs.getString("EMP_ID")
        ));
    }

    public List<EmployeeDto> getEmployees() {
        String sql = "SELECT EMP_ID, NAME, EMAIL, FLOOR_SEAT_SEQ FROM EMPLOYEE ORDER BY EMP_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new EmployeeDto(
                rs.getString("EMP_ID"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                rs.getObject("FLOOR_SEAT_SEQ", Integer.class)
        ));
    }

@Transactional
public void changeSeat(String empId, Integer newSeatSeq) {

    try {

        jdbcTemplate.update(
                "EXEC SP_CHANGE_SEAT ?, ?",
                empId,
                newSeatSeq
        );

    } catch (Exception e) {

        throw new IllegalArgumentException(
                "座位調整失敗：" + e.getMessage()
        );

    }

}
}
