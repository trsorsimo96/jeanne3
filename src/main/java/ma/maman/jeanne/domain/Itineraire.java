package ma.maman.jeanne.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Itineraire.
 */
@Entity
@Table(name = "itineraire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "itineraire")
public class Itineraire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_depart", nullable = false)
    private ZonedDateTime dateDepart;

    @NotNull
    @Column(name = "date_arrive", nullable = false)
    private ZonedDateTime dateArrive;

    @OneToMany(mappedBy = "itinerary")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Voyage> voyages = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Routes route;

    @ManyToOne(optional = false)
    @NotNull
    private Classe classe;

    @ManyToOne(optional = false)
    @NotNull
    private Company company;

    @ManyToOne(optional = false)
    @NotNull
    private Day day;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateDepart() {
        return dateDepart;
    }

    public Itineraire dateDepart(ZonedDateTime dateDepart) {
        this.dateDepart = dateDepart;
        return this;
    }

    public void setDateDepart(ZonedDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public ZonedDateTime getDateArrive() {
        return dateArrive;
    }

    public Itineraire dateArrive(ZonedDateTime dateArrive) {
        this.dateArrive = dateArrive;
        return this;
    }

    public void setDateArrive(ZonedDateTime dateArrive) {
        this.dateArrive = dateArrive;
    }

    public Set<Voyage> getVoyages() {
        return voyages;
    }

    public Itineraire voyages(Set<Voyage> voyages) {
        this.voyages = voyages;
        return this;
    }

    public Itineraire addVoyage(Voyage voyage) {
        this.voyages.add(voyage);
        voyage.setItinerary(this);
        return this;
    }

    public Itineraire removeVoyage(Voyage voyage) {
        this.voyages.remove(voyage);
        voyage.setItinerary(null);
        return this;
    }

    public void setVoyages(Set<Voyage> voyages) {
        this.voyages = voyages;
    }

    public Routes getRoute() {
        return route;
    }

    public Itineraire route(Routes routes) {
        this.route = routes;
        return this;
    }

    public void setRoute(Routes routes) {
        this.route = routes;
    }

    public Classe getClasse() {
        return classe;
    }

    public Itineraire classe(Classe classe) {
        this.classe = classe;
        return this;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Company getCompany() {
        return company;
    }

    public Itineraire company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Day getDay() {
        return day;
    }

    public Itineraire day(Day day) {
        this.day = day;
        return this;
    }

    public void setDay(Day day) {
        this.day = day;
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
        Itineraire itineraire = (Itineraire) o;
        if (itineraire.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itineraire.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Itineraire{" +
            "id=" + getId() +
            ", dateDepart='" + getDateDepart() + "'" +
            ", dateArrive='" + getDateArrive() + "'" +
            "}";
    }
}
