package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
