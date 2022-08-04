package com.spdu.app.user.service;

import com.spdu.app.confirmation_token.model.ConfirmationToken;
import com.spdu.app.confirmation_token.service.TokenService;
import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import com.spdu.app.mail_sending.MailService;
import com.spdu.app.role.Role;
import com.spdu.app.role.service.RoleService;
import com.spdu.app.schedule.model.Schedule;
import com.spdu.app.schedule.service.ScheduleService;
import com.spdu.app.time_slot.model.TimeSlot;
import com.spdu.app.time_slot.service.TimeSlotService;
import com.spdu.app.user.excepton.UserDontExistException;
import com.spdu.app.user.model.User;
import com.spdu.app.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ScheduleService scheduleService;
    private final TimeSlotService timeSlotService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${timeSlotGeneration.daysAmount}")
    private int amountOfDayGenerating;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           ScheduleService scheduleService,
                           TimeSlotService timeSlotService,
                           TokenService tokenService,
                           BCryptPasswordEncoder passwordEncoder,
                           MailService mailService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.scheduleService = scheduleService;
        this.timeSlotService = timeSlotService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public User save(User user) {
        validateUserIfPresent(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("User with id {} saved to db", user.getId());
        provideRole(user);
        sendConfirmationToken(user);
        return user;
    }

    @Override
    public User get(int id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserDontExistException("user with id " + id + " dont exist");
        }
    }

    @Override
    public List<User> getList() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersBySalon(HairdressingSalon salon) {
        return userRepository.findBySalonId(salon.getId());
    }

    @Override
    public List<User> findCustomersBySalonId(Integer salonId) {
        return userRepository.findCustomersBySalonId(salonId);
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public void generateTimeSlotForUser(List<Schedule> schedules, User user) {
        List<TimeSlot> timeSlots = timeSlotService.findAllByUserId(user.getId());
        LocalDate date = getLastDateOrElseCurrent(timeSlots);
        for (int i = 0; i < amountOfDayGenerating; i++) {
            LocalDate futureDate = date.plusDays(i);

            DayOfWeek dayOfWeek = futureDate.getDayOfWeek();
            Schedule schedule = getScheduleAtDayOfWeek(schedules, dayOfWeek);

            if (schedule.getTimeFrom() == null) {
                continue;
            }

            LocalTime time = schedule.getTimeFrom();
            while (time.isBefore(schedule.getTimeTo())) {
                TimeSlot timeSlot = createTimeSlot(user, futureDate, time);
                timeSlotService.save(timeSlot);
                time = time.plusHours(1);
            }
        }
    }

    private void validateUserIfPresent(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("User with this username already exist");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exist");
        }
    }

    private void sendConfirmationToken(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        tokenService.save(confirmationToken);
        mailService.sendVerificationCode(user, confirmationToken);
    }

    private void provideRole(User user) {
        roleService.saveWithDefaultRole(user);
        if (user.getSalonId() != null) {
            roleService.save(user, Role.EMPLOYEE);
            List<Schedule> schedules = scheduleService.findAllBySalonId(user.getSalonId());
            generateTimeSlotForUser(schedules, user);
        }
    }

    private Schedule getScheduleAtDayOfWeek(List<Schedule> schedules, DayOfWeek dayOfWeek) {
        return schedules.stream()
                .filter(schedule -> schedule.getDay().equals(dayOfWeek))
                .findFirst()
                .orElseThrow();
    }

    private TimeSlot createTimeSlot(User user, LocalDate date, LocalTime time) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDay(date);
        timeSlot.setTimeStart(time);
        timeSlot.setTimeEnd(time.plusHours(1));
        timeSlot.setEmployeeId(user.getId());

        return timeSlot;
    }

    private LocalDate getLastDateOrElseCurrent(List<TimeSlot> timeSlots) {
        LocalDate date = LocalDate.now();
        if (!timeSlots.isEmpty()) {
            TimeSlot lastSlot = timeSlots.get(timeSlots.size() - 1);
            date = lastSlot.getDay().plusDays(1);
        }

        return date;
    }
}
