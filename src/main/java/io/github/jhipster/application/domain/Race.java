package io.github.jhipster.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Race.
 */
@Entity
@Table(name = "race")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Race implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "race")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resultat> blogs = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Race title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Resultat> getBlogs() {
        return blogs;
    }

    public Race blogs(Set<Resultat> resultats) {
        this.blogs = resultats;
        return this;
    }

    public Race addBlog(Resultat resultat) {
        this.blogs.add(resultat);
        resultat.setRace(this);
        return this;
    }

    public Race removeBlog(Resultat resultat) {
        this.blogs.remove(resultat);
        resultat.setRace(null);
        return this;
    }

    public void setBlogs(Set<Resultat> resultats) {
        this.blogs = resultats;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Race race = (Race) o;
        if (race.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), race.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Race{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
