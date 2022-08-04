package com.spdu.app.hairdressing_salon.controller;

import com.spdu.app.hairdressing_salon.service.HairdressingSalonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SalonController {
    private final HairdressingSalonService salonService;

    public SalonController(HairdressingSalonService salonService) {
        this.salonService = salonService;
    }

    @GetMapping("/salons")
    public String getSalons(Model model) {
        model.addAttribute("salons", salonService.getList());
        return "hairdressingSalons";
    }
}
