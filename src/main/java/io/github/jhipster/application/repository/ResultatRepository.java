package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Resultat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Resultat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultatRepository extends JpaRepository<Resultat, Long> {

    @Query("select resultat from Resultat resultat where resultat.user.login = ?#{principal.username}")
    List<Resultat> findByUserIsCurrentUser();

}
