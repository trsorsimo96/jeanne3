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

import ma.maman.jeanne.domain.enumeration.TypeVoyage;

import ma.maman.jeanne.domain.enumeration.StateVoyage;

/**
 * A Voyage.
 */
@Entity
@Table(name = "voyage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "voyage")
public class Voyage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "datedepart")
    private ZonedDateTime datedepart;

    @Column(name = "datearrive")
    private ZonedDateTime datearrive;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private TypeVoyage type;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private StateVoyage state;

    @OneToMany(mappedBy = "voyage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Segment> segments = new HashSet<>();

    @ManyToOne
    private Routes route;

    @ManyToOne(optional = false)
    @NotNull
    private Car car;

    @ManyToOne(optional = false)
    @NotNull
    private Train train;

    @ManyToOne
    private Itineraire itinerary;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public Voyage numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public ZonedDateTime getDatedepart() {
        return datedepart;
    }

    public Voyage datedepart(ZonedDateTime datedepart) {
        this.datedepart = datedepart;
        return this;
    }

    public void setDatedepart(ZonedDateTime datedepart) {
        this.datedepart = datedepart;
    }

    public ZonedDateTime getDatearrive() {
        return datearrive;
    }

    public Voyage datearrive(ZonedDateTime datearrive) {
        this.datearrive = datearrive;
        return this;
    }

    public void setDatearrive(ZonedDateTime datearrive) {
        this.datearrive = datearrive;
    }

    public TypeVoyage getType() {
        return type;
    }

    public Voyage type(TypeVoyage type) {
        this.type = type;
        return this;
    }

    public void setType(TypeVoyage type) {
        this.type = type;
    }

    public StateVoyage getState() {
        return state;
    }

    public Voyage state(StateVoyage state) {
        this.state = state;
        return this;
    }

    public void setState(StateVoyage state) {
        this.state = state;
    }

    public Set<Segment> getSegments() {
        return segments;
    }

    public Voyage segments(Set<Segment> segments) {
        this.segments = segments;
        return this;
    }

    public Voyage addSegment(Segment segment) {
        this.segments.add(segment);
        segment.setVoyage(this);
        return this;
    }

    public Voyage removeSegment(Segment segment) {
        this.segments.remove(segment);
        segment.setVoyage(null);
        return this;
    }

    public void setSegments(Set<Segment> segments) {
        this.segments = segments;
    }

    public Routes getRoute() {
        return route;
    }

    public Voyage route(Routes routes) {
        this.route = routes;
        return this;
    }

    public void setRoute(Routes routes) {
        this.route = routes;
    }

    public Car getCar() {
        return car;
    }

    public Voyage car(Car car) {
        this.car = car;
        return this;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Train getTrain() {
        return train;
    }

    public Voyage train(Train train) {
        this.train = train;
        return this;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Itineraire getItinerary() {
        return itinerary;
    }

    public Voyage itinerary(Itineraire itineraire) {
        this.itinerary = itineraire;
        return this;
    }

    public void setItinerary(Itineraire itineraire) {
        this.itinerary = itineraire;
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
        Voyage voyage = (Voyage) o;
        if (voyage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), voyage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Voyage{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", datedepart='" + getDatedepart() + "'" +
            ", datearrive='" + getDatearrive() + "'" +
            ", type='" + getType() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
