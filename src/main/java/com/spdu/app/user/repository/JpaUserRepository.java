package com.spdu.app.user.repository;

import com.spdu.app.user.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile(value = "jpa")
public interface JpaUserRepository extends CrudRepository<User, Long>, UserRepository {
}
