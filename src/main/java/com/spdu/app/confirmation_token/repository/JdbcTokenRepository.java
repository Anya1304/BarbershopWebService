package com.spdu.app.confirmation_token.repository;

import com.spdu.app.confirmation_token.mapper.TokenMapper;
import com.spdu.app.confirmation_token.model.ConfirmationToken;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcTokenRepository implements TokenRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TokenMapper tokenMapper;

    public JdbcTokenRepository(NamedParameterJdbcTemplate jdbcTemplate, TokenMapper tokenMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tokenMapper = tokenMapper;
    }

    @Override
    public void save(ConfirmationToken confirmationToken) {
        SqlParameterSource parameterSource = getMapSqlParameterSource(confirmationToken);
        String query = "INSERT INTO confirmation_token (token, created_date,is_confirm ,user_id) " +
                "VALUES (:token, :createdDate, :isConfirm,:userId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, parameterSource, keyHolder, new String[]{"id"});
        confirmationToken.setTokenId(keyHolder.getKey().intValue());
    }

    @Override
    public ConfirmationToken findByToken(String token) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("token", token);
        String query = "SELECT * FROM confirmation_token WHERE token = :token";

        return jdbcTemplate.queryForObject(query, parameterSource, tokenMapper);
    }

    @Override
    public ConfirmationToken findByUserId(Integer userId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("userId", userId);
        String query = "SELECT * FROM confirmation_token WHERE user_id = :userId";

        return jdbcTemplate.queryForObject(query, parameterSource, tokenMapper);
    }

    @Override
    public void updateConfirmColumnSetTrue(ConfirmationToken token) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("tokenId", token.getTokenId());
        String query = "UPDATE confirmation_token SET is_confirm = 'TRUE' WHERE id = :tokenId";
        jdbcTemplate.update(query, parameterSource);
    }

    @Override
    public List<ConfirmationToken> findAllNonConfirmTokens() {
        String query = "SELECT * FROM confirmation_token WHERE is_confirm = 'false'";

        return jdbcTemplate.query(query, tokenMapper);
    }

    @Override
    public void delete(ConfirmationToken token) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("tokenId", token.getTokenId());
        String query = "DELETE FROM confirmation_token WHERE id = :tokenId";
        jdbcTemplate.update(query, parameterSource);
    }

    private MapSqlParameterSource getMapSqlParameterSource(ConfirmationToken confirmationToken) {
        return new MapSqlParameterSource()
                .addValue("token", confirmationToken.getConfirmationToken())
                .addValue("createdDate", confirmationToken.getCreatedDate())
                .addValue("isConfirm", confirmationToken.isConfirm())
                .addValue("userId", confirmationToken.getUser().getId());
    }
}
