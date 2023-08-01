package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.resources.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ResourcesRepository extends JpaRepository <Resources, Long> {
}
