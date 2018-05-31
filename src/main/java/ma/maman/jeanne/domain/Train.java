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
 * A Train.
 */
@Entity
@Table(name = "train")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "train")
public class Train implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "place")
    private Integer place;

    @OneToMany(mappedBy = "train")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Wagon> wagons = new HashSet<>();

    @OneToMany(mappedBy = "train")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Voyage> voyages = new HashSet<>();

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

    public String getCode() {
        return code;
    }

    public Train code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Train name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlace() {
        return place;
    }

    public Train place(Integer place) {
        this.place = place;
        return this;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Set<Wagon> getWagons() {
        return wagons;
    }

    public Train wagons(Set<Wagon> wagons) {
        this.wagons = wagons;
        return this;
    }

    public Train addWagon(Wagon wagon) {
        this.wagons.add(wagon);
        wagon.setTrain(this);
        return this;
    }

    public Train removeWagon(Wagon wagon) {
        this.wagons.remove(wagon);
        wagon.setTrain(null);
        return this;
    }

    public void setWagons(Set<Wagon> wagons) {
        this.wagons = wagons;
    }

    public Set<Voyage> getVoyages() {
        return voyages;
    }

    public Train voyages(Set<Voyage> voyages) {
        this.voyages = voyages;
        return this;
    }

    public Train addVoyage(Voyage voyage) {
        this.voyages.add(voyage);
        voyage.setTrain(this);
        return this;
    }

    public Train removeVoyage(Voyage voyage) {
        this.voyages.remove(voyage);
        voyage.setTrain(null);
        return this;
    }

    public void setVoyages(Set<Voyage> voyages) {
        this.voyages = voyages;
    }

    public Company getCompany() {
        return company;
    }

    public Train company(Company company) {
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
        Train train = (Train) o;
        if (train.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), train.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Train{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", place=" + getPlace() +
            "}";
    }
}
