package com.spdu.app.time_slot.service;

import com.spdu.app.time_slot.model.TimeSlot;

import java.util.List;

public interface TimeSlotService {
    TimeSlot save(TimeSlot obj);

    List<TimeSlot> findAllByUserId(Integer userId);

    TimeSlot findById(int id);

    void delete(int id);

    void saveOrderIntoTimeSlot(int orderId, int slotId);
}
