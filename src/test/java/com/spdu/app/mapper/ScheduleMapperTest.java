package com.spdu.app.mapper;

import com.spdu.app.schedule.mapper.ScheduleMapper;
import com.spdu.app.schedule.model.Schedule;
import org.h2.tools.SimpleResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ScheduleMapperTest {
    private ScheduleMapper scheduleMapper;

    @BeforeEach
    void setUp() {
        scheduleMapper = new ScheduleMapper();
    }

    @Test
    public void whenMapFromResultSetThenShouldReturnSchedule() throws Exception {
        Schedule scheduleExpected = createSchedule(1, DayOfWeek.MONDAY, LocalTime.of(8, 0),
                LocalTime.of(21, 0), 1);
        SimpleResultSet resultSet = mock(SimpleResultSet.class);
        given(resultSet.getInt("id")).willReturn(scheduleExpected.getId());
        given(resultSet.getString("day")).willReturn(scheduleExpected.getDay().toString());
        given(resultSet.getString("time_from")).willReturn(scheduleExpected.getTimeFrom().toString());
        given(resultSet.getString("time_to")).willReturn(scheduleExpected.getTimeTo().toString());
        given(resultSet.getObject("hairdressing_salon_id", Integer.class))
                .willReturn(scheduleExpected.getHairdressingSalonId());

        Schedule scheduleActual = scheduleMapper.mapRow(resultSet, resultSet.getRow());
        assertThat(scheduleActual).isNotNull();
        assertThat(scheduleActual).isEqualTo(scheduleExpected);
    }

    private Schedule createSchedule(int id, DayOfWeek day, LocalTime timeFrom,
                                    LocalTime timeTo, int salonId) {
        Schedule schedule = new Schedule();
        schedule.setId(id);
        schedule.setDay(day);
        schedule.setTimeFrom(timeFrom);
        schedule.setTimeTo(timeTo);
        schedule.setHairdressingSalonId(salonId);
        return schedule;
    }
}
