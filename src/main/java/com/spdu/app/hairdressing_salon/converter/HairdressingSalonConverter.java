package com.spdu.app.hairdressing_salon.converter;

import com.spdu.app.hairdressing_salon.dto.HairdressingSalonDto;
import com.spdu.app.hairdressing_salon.model.HairdressingSalon;

import java.util.List;

public final class HairdressingSalonConverter {

    private HairdressingSalonConverter() {
    }

    public static HairdressingSalonDto toDto(HairdressingSalon salon) {
        HairdressingSalonDto salonDto = new HairdressingSalonDto();

        salonDto.setId(salon.getId());
        salonDto.setAddress(salon.getAddress());
        salonDto.setEmail(salon.getEmail());
        salonDto.setDescription(salon.getDescription());
        salonDto.setName(salon.getName());

        return salonDto;
    }

    public static HairdressingSalon fromDto(HairdressingSalonDto salonDto) {
        HairdressingSalon salon = new HairdressingSalon();

        salon.setId(salonDto.getId());
        salon.setEmail(salonDto.getEmail());
        salon.setDescription(salonDto.getDescription());
        salon.setName(salonDto.getName());
        salon.setAddress(salonDto.getAddress());

        return salon;
    }

    public static List<HairdressingSalonDto> toDto(List<HairdressingSalon> salons) {
        return salons.stream()
                .map(HairdressingSalonConverter::toDto)
                .toList();
    }
}

