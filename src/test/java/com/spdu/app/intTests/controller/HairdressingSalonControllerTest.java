package com.spdu.app.intTests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spdu.app.intTests.controller.repository.SalonTestRepository;
import com.spdu.app.intTests.controller.repository.ScheduleTestRepository;
import com.spdu.app.hairdressing_salon.converter.HairdressingSalonConverter;
import com.spdu.app.hairdressing_salon.dto.HairdressingSalonDto;
import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import com.spdu.app.schedule.model.Schedule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "user", password = "user", authorities = {"USER", "ADMIN"})
class HairdressingSalonControllerTest {
    private static final String TEST_NAME1 = "test1";
    private static final String TEST_ADDRESS1 = "test1@test.com";
    private static final String TEST_DESCRIPTION1 = "test description";
    private static final String TEST_EMAIL1 = "test1@test.com";

    @Autowired
    SalonTestRepository hairdressingSalonRepository;

    @Autowired
    ScheduleTestRepository scheduleRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void beforeEach() {
        hairdressingSalonRepository.deleteAll();
    }

    @Test
    void whenAddSalonThenShouldReturnSameSalon() throws Exception {
        //GIVEN
        HairdressingSalonDto salonExpected = createSalonDto();

        //WHEN
        String salonJson = mockMvc.perform(post("/api/add-hairdressing-salon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(salonExpected)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HairdressingSalonDto salonActual = objectMapper.readValue(salonJson, HairdressingSalonDto.class);

        List<HairdressingSalon> hairdressingSalons = hairdressingSalonRepository.findAll();

        //THEN
        assertThat(salonActual.getName()).isEqualTo(salonExpected.getName());
        assertThat(salonActual.getEmail()).isEqualTo(salonExpected.getEmail());
        assertThat(salonActual.getDescription()).isEqualTo(salonExpected.getDescription());
        assertThat(salonActual.getAddress()).isEqualTo(salonExpected.getAddress());

        assertThat(hairdressingSalons).hasSize(1);
    }

    @Test
    void whenGetSalonsThenShouldReturnActualSalonList() throws Exception {
        //GIVEN
        List<HairdressingSalon> salonsExpected = createSalons();
        salonsExpected.forEach(this::saveSalonToDb);

        //WHEN
        String hairdressingSalonJson = mockMvc.perform(get("/api/hairdressing-salons"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<HairdressingSalonDto> salonsDto = Arrays.asList(objectMapper
                .readValue(hairdressingSalonJson, HairdressingSalonDto[].class));

        //THEN
        List<HairdressingSalon> salonsActual = salonsDto
                .stream()
                .map(HairdressingSalonConverter::fromDto).toList();

        assertTrue(salonsExpected.containsAll(salonsActual));
    }

    @Test
    void whenGetSalonThenShouldReturnSameSalon() throws Exception {
        //GIVEN
        HairdressingSalon salonExpected = createSalon(TEST_NAME1, TEST_EMAIL1, TEST_DESCRIPTION1, TEST_ADDRESS1);
        salonExpected = saveSalonToDb(salonExpected);

        //WHEN
        mockMvc.perform(get("/api/hairdressing-salons/" + salonExpected.getId()))
                .andExpect(status().isOk());

        String hairdressingSalonJson = mockMvc
                .perform(get("/api/hairdressing-salons/" + salonExpected.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        HairdressingSalonDto salonActual = objectMapper.readValue(hairdressingSalonJson,
                HairdressingSalonDto.class);

        //THEN
        assertThatSalonEqualsSalonDto(salonActual, salonExpected);
    }

    @Test
    void whenSalonRemovedThenThisSalonDoesntExist() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon(TEST_NAME1, TEST_EMAIL1, TEST_DESCRIPTION1, TEST_ADDRESS1);
        saveSalonToDb(salon);
        //WHEN
        mockMvc.perform(delete("/api/hairdressing-salons/" + salon.getId())).andExpect(status().isOk());

        String hairdressingSalonJson = mockMvc.perform(get("/api/hairdressing-salons"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<HairdressingSalonDto> hairdressingSalons = Arrays.asList(objectMapper
                .readValue(hairdressingSalonJson, HairdressingSalonDto[].class));

        //THEN
        assertTrue(hairdressingSalons.stream().noneMatch(salonDto -> salonDto.getId() == salon.getId()));
    }

    @Test
    void whenAddSalonThenShouldCreateSchedule() throws Exception {
        //GIVEN
        HairdressingSalonDto salonExpected = createSalonDto();

        //WHEN
        String salonJson = mockMvc.perform(post("/api/add-hairdressing-salon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(salonExpected)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HairdressingSalonDto salonActual = objectMapper.readValue(salonJson, HairdressingSalonDto.class);
        List<Schedule> schedules = scheduleRepository.findAllBySalonId(salonActual.getId());

        assertThat(schedules).hasSize(DayOfWeek.values().length);
        for (int day = 1; day <= 7; day++) {
            int finalDay = day;
            assertTrue(schedules.stream().anyMatch(schedule -> schedule.getDay().equals(DayOfWeek.of(finalDay))));
        }
    }

    private HairdressingSalon createSalon(String name, String email, String description, String address) {
        HairdressingSalon hairdressingSalon = new HairdressingSalon();
        hairdressingSalon.setName(name);
        hairdressingSalon.setEmail(email);
        hairdressingSalon.setDescription(description);
        hairdressingSalon.setAddress(address);

        return hairdressingSalon;
    }

    private HairdressingSalonDto createSalonDto() {
        HairdressingSalonDto hairdressingSalonDto = new HairdressingSalonDto();
        hairdressingSalonDto.setName("test1");
        hairdressingSalonDto.setEmail("test1@test.com");
        hairdressingSalonDto.setDescription("test description");
        hairdressingSalonDto.setAddress("test1@test.com");

        return hairdressingSalonDto;
    }

    private HairdressingSalon saveSalonToDb(HairdressingSalon hairdressingSalon) {
        return hairdressingSalonRepository.save(hairdressingSalon);
    }

    private List<HairdressingSalon> createSalons() {
        HairdressingSalon salon1 = createSalon(TEST_NAME1, TEST_EMAIL1,
                TEST_DESCRIPTION1, TEST_ADDRESS1);

        String testName2 = "test2";
        String testAddress2 = "test2@test.com";
        String testDescription2 = "test description";
        String testEmail2 = "test2@test.com";

        HairdressingSalon salon2 = createSalon(testName2, testEmail2,
                testDescription2, testAddress2);

        return List.of(salon1, salon2);
    }

    private void assertThatSalonEqualsSalonDto(HairdressingSalonDto salonActual, HairdressingSalon salonExpected) {
        assertThat(salonActual.getName()).isEqualTo(salonExpected.getName());
        assertThat(salonActual.getAddress()).isEqualTo(salonExpected.getAddress());
        assertThat(salonActual.getDescription()).isEqualTo(salonExpected.getDescription());
        assertThat(salonActual.getEmail()).isEqualTo(salonExpected.getEmail());
    }
}
