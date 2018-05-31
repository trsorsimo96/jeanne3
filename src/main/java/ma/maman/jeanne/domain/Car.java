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
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "car")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "immatriculation", nullable = false)
    private String immatriculation;

    @Column(name = "place")
    private Integer place;

    @OneToMany(mappedBy = "car")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Voyage> voyages = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private ModelCar model;

    @ManyToOne(optional = false)
    @NotNull
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public Car immatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
        return this;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public Integer getPlace() {
        return place;
    }

    public Car place(Integer place) {
        this.place = place;
        return this;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Set<Voyage> getVoyages() {
        return voyages;
    }

    public Car voyages(Set<Voyage> voyages) {
        this.voyages = voyages;
        return this;
    }

    public Car addVoyage(Voyage voyage) {
        this.voyages.add(voyage);
        voyage.setCar(this);
        return this;
    }

    public Car removeVoyage(Voyage voyage) {
        this.voyages.remove(voyage);
        voyage.setCar(null);
        return this;
    }

    public void setVoyages(Set<Voyage> voyages) {
        this.voyages = voyages;
    }

    public ModelCar getModel() {
        return model;
    }

    public Car model(ModelCar modelCar) {
        this.model = modelCar;
        return this;
    }

    public void setModel(ModelCar modelCar) {
        this.model = modelCar;
    }

    public Company getCompany() {
        return company;
    }

    public Car company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        Car car = (Car) o;
        if (car.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), car.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", immatriculation='" + getImmatriculation() + "'" +
            ", place=" + getPlace() +
            "}";
    }
}
