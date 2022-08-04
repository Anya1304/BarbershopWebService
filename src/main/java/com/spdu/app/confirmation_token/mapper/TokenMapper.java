package com.spdu.app.confirmation_token.mapper;

import com.spdu.app.confirmation_token.model.ConfirmationToken;
import com.spdu.app.user.repository.UserRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class TokenMapper implements RowMapper<ConfirmationToken> {
    private final UserRepository userRepository;

    public TokenMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ConfirmationToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        ConfirmationToken confirmationToken = new ConfirmationToken();

        confirmationToken.setTokenId(rs.getObject("id", Integer.class));
        confirmationToken.setConfirmationToken(rs.getObject("token", String.class));

        String day = rs.getString("created_date");
        confirmationToken.setCreatedDate(LocalDate.parse(day));
        confirmationToken.setConfirm(rs.getBoolean("is_confirm"));
        Integer userId = rs.getObject("user_id", Integer.class);
        userRepository.findById(userId).ifPresent(confirmationToken::setUser);

        return confirmationToken;
    }
}
