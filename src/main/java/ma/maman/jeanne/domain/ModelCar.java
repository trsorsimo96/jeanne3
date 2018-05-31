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
 * A ModelCar.
 */
@Entity
@Table(name = "model_car")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "modelcar")
public class ModelCar implements Serializable {

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

    @NotNull
    @Column(name = "nb_normal_range", nullable = false)
    private Integer nbNormalRange;

    @NotNull
    @Column(name = "nb_seat_left", nullable = false)
    private Integer nbSeatLeft;

    @NotNull
    @Column(name = "nb_seat_right", nullable = false)
    private Integer nbSeatRight;

    @Column(name = "nb_seat_before")
    private Integer nbSeatBefore;

    @Column(name = "nb_seat_below")
    private Integer nbSeatBelow;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @OneToMany(mappedBy = "model")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Car> cars = new HashSet<>();

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

    public ModelCar code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public ModelCar name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNbNormalRange() {
        return nbNormalRange;
    }

    public ModelCar nbNormalRange(Integer nbNormalRange) {
        this.nbNormalRange = nbNormalRange;
        return this;
    }

    public void setNbNormalRange(Integer nbNormalRange) {
        this.nbNormalRange = nbNormalRange;
    }

    public Integer getNbSeatLeft() {
        return nbSeatLeft;
    }

    public ModelCar nbSeatLeft(Integer nbSeatLeft) {
        this.nbSeatLeft = nbSeatLeft;
        return this;
    }

    public void setNbSeatLeft(Integer nbSeatLeft) {
        this.nbSeatLeft = nbSeatLeft;
    }

    public Integer getNbSeatRight() {
        return nbSeatRight;
    }

    public ModelCar nbSeatRight(Integer nbSeatRight) {
        this.nbSeatRight = nbSeatRight;
        return this;
    }

    public void setNbSeatRight(Integer nbSeatRight) {
        this.nbSeatRight = nbSeatRight;
    }

    public Integer getNbSeatBefore() {
        return nbSeatBefore;
    }

    public ModelCar nbSeatBefore(Integer nbSeatBefore) {
        this.nbSeatBefore = nbSeatBefore;
        return this;
    }

    public void setNbSeatBefore(Integer nbSeatBefore) {
        this.nbSeatBefore = nbSeatBefore;
    }

    public Integer getNbSeatBelow() {
        return nbSeatBelow;
    }

    public ModelCar nbSeatBelow(Integer nbSeatBelow) {
        this.nbSeatBelow = nbSeatBelow;
        return this;
    }

    public void setNbSeatBelow(Integer nbSeatBelow) {
        this.nbSeatBelow = nbSeatBelow;
    }

    public byte[] getImage() {
        return image;
    }

    public ModelCar image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public ModelCar imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public ModelCar cars(Set<Car> cars) {
        this.cars = cars;
        return this;
    }

    public ModelCar addCar(Car car) {
        this.cars.add(car);
        car.setModel(this);
        return this;
    }

    public ModelCar removeCar(Car car) {
        this.cars.remove(car);
        car.setModel(null);
        return this;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
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
        ModelCar modelCar = (ModelCar) o;
        if (modelCar.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), modelCar.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ModelCar{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", nbNormalRange=" + getNbNormalRange() +
            ", nbSeatLeft=" + getNbSeatLeft() +
            ", nbSeatRight=" + getNbSeatRight() +
            ", nbSeatBefore=" + getNbSeatBefore() +
            ", nbSeatBelow=" + getNbSeatBelow() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
