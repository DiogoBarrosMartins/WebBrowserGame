package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.Grid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GridRepository  extends JpaRepository<Grid, Long> {
    Grid findFirstByOrderByIdAsc();
}
