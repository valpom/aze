package io.github.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.application.domain.Race;
import io.github.jhipster.application.repository.RaceRepository;
import io.github.jhipster.application.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.application.web.rest.util.HeaderUtil;
import io.github.jhipster.application.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Race.
 */
@RestController
@RequestMapping("/api")
public class RaceResource {

    private final Logger log = LoggerFactory.getLogger(RaceResource.class);

    private static final String ENTITY_NAME = "race";

    private RaceRepository raceRepository;

    public RaceResource(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    /**
     * POST  /races : Create a new race.
     *
     * @param race the race to create
     * @return the ResponseEntity with status 201 (Created) and with body the new race, or with status 400 (Bad Request) if the race has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/races")
    @Timed
    public ResponseEntity<Race> createRace(@Valid @RequestBody Race race) throws URISyntaxException {
        log.debug("REST request to save Race : {}", race);
        if (race.getId() != null) {
            throw new BadRequestAlertException("A new race cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Race result = raceRepository.save(race);
        return ResponseEntity.created(new URI("/api/races/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /races : Updates an existing race.
     *
     * @param race the race to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated race,
     * or with status 400 (Bad Request) if the race is not valid,
     * or with status 500 (Internal Server Error) if the race couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/races")
    @Timed
    public ResponseEntity<Race> updateRace(@Valid @RequestBody Race race) throws URISyntaxException {
        log.debug("REST request to update Race : {}", race);
        if (race.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Race result = raceRepository.save(race);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, race.getId().toString()))
            .body(result);
    }

    /**
     * GET  /races : get all the races.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of races in body
     */
    @GetMapping("/races")
    @Timed
    public ResponseEntity<List<Race>> getAllRaces(Pageable pageable) {
        log.debug("REST request to get a page of Races");
        Page<Race> page = raceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/races");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /races/:id : get the "id" race.
     *
     * @param id the id of the race to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the race, or with status 404 (Not Found)
     */
    @GetMapping("/races/{id}")
    @Timed
    public ResponseEntity<Race> getRace(@PathVariable Long id) {
        log.debug("REST request to get Race : {}", id);
        Optional<Race> race = raceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(race);
    }

    /**
     * DELETE  /races/:id : delete the "id" race.
     *
     * @param id the id of the race to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/races/{id}")
    @Timed
    public ResponseEntity<Void> deleteRace(@PathVariable Long id) {
        log.debug("REST request to delete Race : {}", id);

        raceRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
