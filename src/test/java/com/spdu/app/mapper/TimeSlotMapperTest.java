package com.spdu.app.mapper;

import com.spdu.app.time_slot.mapper.TimeSlotMapper;
import com.spdu.app.time_slot.model.TimeSlot;
import org.h2.tools.SimpleResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class TimeSlotMapperTest {
    private TimeSlotMapper timeSlotMapper;

    @BeforeEach
    void setUp() {
        timeSlotMapper = new TimeSlotMapper();
    }

    @Test
    public void whenMapFromResultSetThenShouldReturnTimeSlot() throws Exception {
        TimeSlot timeSlotExpected = createTimeSlot(1, LocalDate.now(), LocalTime.of(8, 0),
                LocalTime.of(9, 0), 1, null);
        SimpleResultSet resultSet = mock(SimpleResultSet.class);
        given(resultSet.getInt("id")).willReturn(timeSlotExpected.getId());
        given(resultSet.getString("day")).willReturn(timeSlotExpected.getDay().toString());
        given(resultSet.getString("time_start")).willReturn(timeSlotExpected.getTimeStart().toString());
        given(resultSet.getString("time_end")).willReturn(timeSlotExpected.getTimeEnd().toString());
        given(resultSet.getObject("employee_id", Integer.class)).willReturn(timeSlotExpected.getEmployeeId());
        given(resultSet.getObject("order_id", Integer.class)).willReturn(timeSlotExpected.getOrderId());

        TimeSlot timeSlotActual = timeSlotMapper.mapRow(resultSet, resultSet.getRow());
        assertThat(timeSlotActual).isNotNull();
        assertThat(timeSlotActual).isEqualTo(timeSlotExpected);
    }

    private TimeSlot createTimeSlot(int id, LocalDate date, LocalTime timeStart,
                                    LocalTime timeEnd, int employeeId, Integer orderId) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(id);
        timeSlot.setDay(date);
        timeSlot.setTimeStart(timeStart);
        timeSlot.setTimeEnd(timeEnd);
        timeSlot.setEmployeeId(employeeId);
        timeSlot.setOrderId(orderId);
        return timeSlot;
    }
}
