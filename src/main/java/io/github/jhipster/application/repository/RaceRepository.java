package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Race;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Race entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

}
