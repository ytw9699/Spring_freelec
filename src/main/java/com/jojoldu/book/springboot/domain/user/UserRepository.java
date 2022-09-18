package com.jojoldu.book.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {//User의 CRUD를 책임짐

    Optional<User> findByEmail(String email);
}
