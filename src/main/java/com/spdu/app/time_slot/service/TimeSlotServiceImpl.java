package com.spdu.app.time_slot.service;

import com.spdu.app.orders.exception.TimeSlotAlreadyBusyException;
import com.spdu.app.time_slot.exception.TimeWasWrongException;
import com.spdu.app.time_slot.model.TimeSlot;
import com.spdu.app.time_slot.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    private final Logger logger = LoggerFactory.getLogger(TimeSlotServiceImpl.class);

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public TimeSlot save(TimeSlot timeSlot) {
        timeCheck(timeSlot);
        return timeSlotRepository.save(timeSlot);
    }

    @Override
    public List<TimeSlot> findAllByUserId(Integer id) {
        return timeSlotRepository.findAllByUserId(id);
    }

    @Override
    public TimeSlot findById(int id) {
        return timeSlotRepository.findById(id);
    }

    @Override
    public void delete(int id) {
        timeSlotRepository.deleteById(id);
        logger.info("Delete timeslot with id {} was successful", id);
    }

    @Override
    public void saveOrderIntoTimeSlot(int orderId, int slotId) {
        checkTimeSlotForBusy(slotId);
        timeSlotRepository.saveIntoTimeSlot(orderId, slotId);
        logger.info("Save Order id: {}, into time Slot id: {} ", orderId, slotId);
    }

    private void checkTimeSlotForBusy(int timeSlotId) {
        TimeSlot timeSlot = findById(timeSlotId);
        boolean isBusy = timeSlot.getOrderId() != null;
        if (isBusy) {
            throw new TimeSlotAlreadyBusyException("timeSlot with id " + timeSlotId +
                    " was already busy, please choose another");
        }
    }

    private void timeCheck(TimeSlot timeSlot) {
        if (timeSlot.getDay().isBefore(LocalDate.now())) {
            throw new TimeWasWrongException("Day must be after now");
        } else {
            if (timeSlot.getTimeStart().isAfter(timeSlot.getTimeEnd())) {
                throw new TimeWasWrongException("Start Time should be before Time End");
            }
            if (Duration.between(timeSlot.getTimeStart(), timeSlot.getTimeEnd()).toHours() != 1) {
                throw new TimeWasWrongException("Duration must be 1 hour");
            }
        }
    }
}