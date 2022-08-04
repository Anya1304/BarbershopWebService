package com.spdu.app.hairdressing_salon.controller;

import com.spdu.app.hairdressing_salon.dto.HairdressingSalonDto;
import com.spdu.app.hairdressing_salon.service.HairdressingSalonServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.spdu.app.hairdressing_salon.converter.HairdressingSalonConverter.fromDto;
import static com.spdu.app.hairdressing_salon.converter.HairdressingSalonConverter.toDto;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "salon")
public class HairdressingSalonRestController {
    private final HairdressingSalonServiceImpl hairdressingSalonService;

    public HairdressingSalonRestController(HairdressingSalonServiceImpl hairdressingSalonService) {
        this.hairdressingSalonService = hairdressingSalonService;
    }

    @PostMapping("/add-hairdressing-salon")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public HairdressingSalonDto postSalons(@RequestBody @Valid HairdressingSalonDto salonDto) {
        return toDto(hairdressingSalonService.save(fromDto(salonDto)));
    }

    @GetMapping("/hairdressing-salons")
    @PreAuthorize("permitAll")
    public List<HairdressingSalonDto> getSalons() {
        return toDto(hairdressingSalonService.getList());
    }

    @GetMapping("/hairdressing-salons/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public HairdressingSalonDto getSalon(@PathVariable int id) {
        return toDto(hairdressingSalonService.get(id));
    }

    @DeleteMapping("/hairdressing-salons/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSalon(@PathVariable int id) {
        hairdressingSalonService.delete(id);
    }
}
