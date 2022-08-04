package com.spdu.app.user.controller;

import com.spdu.app.confirmation_token.model.ConfirmationToken;
import com.spdu.app.confirmation_token.service.TokenService;
import com.spdu.app.hairdressing_salon.service.HairdressingSalonService;
import com.spdu.app.orders.model.Order;
import com.spdu.app.orders.repository.OrderRepository;
import com.spdu.app.spring_security.SecurityUser;
import com.spdu.app.time_slot.service.TimeSlotService;
import com.spdu.app.user.model.User;
import com.spdu.app.user.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final HairdressingSalonService salonService;
    private final OrderRepository orderService;
    private final TimeSlotService timeSlotService;

    public UserController(UserService userService,
                          TokenService tokenService,
                          HairdressingSalonService salonService,
                          OrderRepository orderService, TimeSlotService timeSlotService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.salonService = salonService;
        this.orderService = orderService;
        this.timeSlotService = timeSlotService;
    }

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/signup")
    public String addUser(@ModelAttribute("user") @Valid User user, Model model) {
        userService.save(user);
        model.addAttribute("user", user);
        return "validation";
    }

    @GetMapping("/confirm-account")
    public String confirmAccount(@RequestParam("token") String token) {
        ConfirmationToken confirmationToken = tokenService.findByToken(token);
        if (confirmationToken != null) {
            tokenService.updateConfirmColumnSetTrue(confirmationToken);
        }
        return "successfulRegister";
    }

    @GetMapping("/barbers/{salonId}")
    public String getEmployees(@PathVariable int salonId, Model model) {
        List<User> users = userService.getUsersBySalon(salonService.get(salonId));
        model.addAttribute("employees", users);
        return "employees";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = ((SecurityUser) principal).getUser();
        List<Order> orders = orderService.findAllByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "userProfile";
    }
}
