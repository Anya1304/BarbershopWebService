package com.spdu.app.hairdressing_salon.mapper;

import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class HairdressingSalonMapper implements RowMapper<HairdressingSalon> {

    @Override
    public HairdressingSalon mapRow(ResultSet rs, int rowNum) throws SQLException {
        HairdressingSalon hairdressingSalon = new HairdressingSalon();

        hairdressingSalon.setId(rs.getInt("id"));
        hairdressingSalon.setName(rs.getString("name"));
        hairdressingSalon.setEmail(rs.getString("email"));
        hairdressingSalon.setDescription(rs.getString("description"));
        hairdressingSalon.setAddress(rs.getString("address"));

        return hairdressingSalon;
    }
}
