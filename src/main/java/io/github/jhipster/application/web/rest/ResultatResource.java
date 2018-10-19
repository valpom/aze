package io.github.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.application.domain.Resultat;
import io.github.jhipster.application.repository.ResultatRepository;
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
 * REST controller for managing Resultat.
 */
@RestController
@RequestMapping("/api")
public class ResultatResource {

    private final Logger log = LoggerFactory.getLogger(ResultatResource.class);

    private static final String ENTITY_NAME = "resultat";

    private ResultatRepository resultatRepository;

    public ResultatResource(ResultatRepository resultatRepository) {
        this.resultatRepository = resultatRepository;
    }

    /**
     * POST  /resultats : Create a new resultat.
     *
     * @param resultat the resultat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resultat, or with status 400 (Bad Request) if the resultat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resultats")
    @Timed
    public ResponseEntity<Resultat> createResultat(@Valid @RequestBody Resultat resultat) throws URISyntaxException {
        log.debug("REST request to save Resultat : {}", resultat);
        if (resultat.getId() != null) {
            throw new BadRequestAlertException("A new resultat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Resultat result = resultatRepository.save(resultat);
        return ResponseEntity.created(new URI("/api/resultats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resultats : Updates an existing resultat.
     *
     * @param resultat the resultat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resultat,
     * or with status 400 (Bad Request) if the resultat is not valid,
     * or with status 500 (Internal Server Error) if the resultat couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resultats")
    @Timed
    public ResponseEntity<Resultat> updateResultat(@Valid @RequestBody Resultat resultat) throws URISyntaxException {
        log.debug("REST request to update Resultat : {}", resultat);
        if (resultat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Resultat result = resultatRepository.save(resultat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resultat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resultats : get all the resultats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resultats in body
     */
    @GetMapping("/resultats")
    @Timed
    public ResponseEntity<List<Resultat>> getAllResultats(Pageable pageable) {
        log.debug("REST request to get a page of Resultats");
        Page<Resultat> page = resultatRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resultats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resultats/:id : get the "id" resultat.
     *
     * @param id the id of the resultat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resultat, or with status 404 (Not Found)
     */
    @GetMapping("/resultats/{id}")
    @Timed
    public ResponseEntity<Resultat> getResultat(@PathVariable Long id) {
        log.debug("REST request to get Resultat : {}", id);
        Optional<Resultat> resultat = resultatRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resultat);
    }

    /**
     * DELETE  /resultats/:id : delete the "id" resultat.
     *
     * @param id the id of the resultat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resultats/{id}")
    @Timed
    public ResponseEntity<Void> deleteResultat(@PathVariable Long id) {
        log.debug("REST request to delete Resultat : {}", id);

        resultatRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
