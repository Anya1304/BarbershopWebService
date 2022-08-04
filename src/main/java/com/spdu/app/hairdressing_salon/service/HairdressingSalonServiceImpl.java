package com.spdu.app.hairdressing_salon.service;

import com.spdu.app.hairdressing_salon.exception.SalonDoesntExistException;
import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import com.spdu.app.hairdressing_salon.repository.HairdressingSalonRepository;
import com.spdu.app.mail_sending.MailService;
import com.spdu.app.schedule.model.Schedule;
import com.spdu.app.schedule.service.ScheduleService;
import com.spdu.app.user.model.User;
import com.spdu.app.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class HairdressingSalonServiceImpl implements HairdressingSalonService {
    private final Logger logger = LoggerFactory.getLogger(HairdressingSalonServiceImpl.class);
    private final HairdressingSalonRepository hairdressingSalonRepository;
    private final ScheduleService scheduleService;
    private final MailService mailService;
    private final UserService userService;

    public HairdressingSalonServiceImpl(HairdressingSalonRepository hairdressingSalonRepository,
                                        ScheduleService scheduleService, MailService mailService,
                                        UserService userService) {
        this.hairdressingSalonRepository = hairdressingSalonRepository;
        this.scheduleService = scheduleService;
        this.mailService = mailService;
        this.userService = userService;
    }

    @Override
    public HairdressingSalon save(HairdressingSalon hairdressingSalon) {
        hairdressingSalonRepository.save(hairdressingSalon);
        saveDefaultScheduleForSalon(hairdressingSalon);
        return hairdressingSalon;
    }

    @Override
    public HairdressingSalon get(int id) {
        Optional<HairdressingSalon> salonOptional = hairdressingSalonRepository.findById(id);
        if (salonOptional.isPresent()) {
            return salonOptional.get();
        } else {
            throw new SalonDoesntExistException("salon with id " + id + " dont exist");
        }
    }

    @Override
    public List<HairdressingSalon> getList() {
        return hairdressingSalonRepository.findAll();
    }

    @Override
    public void delete(int id) {
        List<User> customers = userService.findCustomersBySalonId(id);
        notificationUsersAboutClosingSalon(customers);
        hairdressingSalonRepository.deleteById(id);
    }

    private void saveDefaultScheduleForSalon(HairdressingSalon hairdressingSalon) {
        for (int dayNum = 1; dayNum <= DayOfWeek.values().length; dayNum++) {
            Schedule schedule = new Schedule();
            schedule.setDay(DayOfWeek.of(dayNum));
            schedule.setHairdressingSalonId(hairdressingSalon.getId());
            scheduleService.save(schedule);
        }
    }

    private void notificationUsersAboutClosingSalon(List<User> customers) {
        for (User customer : customers) {
            mailService.sendClosedSalonNotification(customer);
        }
        logger.info("Total count of closing salon messages {}", mailService.getCountOfCancelMessages());
    }
}
