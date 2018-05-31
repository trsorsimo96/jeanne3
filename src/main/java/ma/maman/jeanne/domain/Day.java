package ma.maman.jeanne.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Day.
 */
@Entity
@Table(name = "day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "day")
public class Day implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "code", length = 3, nullable = false)
    private String code;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    @Column(name = "jhi_number", nullable = false)
    private Integer number;

    @OneToMany(mappedBy = "day")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Itineraire> itineraires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Day code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public Day text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNumber() {
        return number;
    }

    public Day number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Set<Itineraire> getItineraires() {
        return itineraires;
    }

    public Day itineraires(Set<Itineraire> itineraires) {
        this.itineraires = itineraires;
        return this;
    }

    public Day addItineraire(Itineraire itineraire) {
        this.itineraires.add(itineraire);
        itineraire.setDay(this);
        return this;
    }

    public Day removeItineraire(Itineraire itineraire) {
        this.itineraires.remove(itineraire);
        itineraire.setDay(null);
        return this;
    }

    public void setItineraires(Set<Itineraire> itineraires) {
        this.itineraires = itineraires;
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
        Day day = (Day) o;
        if (day.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), day.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Day{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", text='" + getText() + "'" +
            ", number=" + getNumber() +
            "}";
    }
}
