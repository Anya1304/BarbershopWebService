package com.spdu.app.time_slot.repository;

import com.spdu.app.time_slot.model.TimeSlot;

import java.util.List;

public interface TimeSlotRepository {
    TimeSlot save(TimeSlot obj);

    List<TimeSlot> findAllByUserId(Integer userId);

    TimeSlot findById(Integer id);

    void saveIntoTimeSlot(int orderId, int slotId);

    void deleteById(int id);
}
