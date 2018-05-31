package ma.maman.jeanne.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Fare.
 */
@Entity
@Table(name = "fare")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fare")
public class Fare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "segment_number", nullable = false)
    private Integer segmentNumber;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @ManyToOne(optional = false)
    @NotNull
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSegmentNumber() {
        return segmentNumber;
    }

    public Fare segmentNumber(Integer segmentNumber) {
        this.segmentNumber = segmentNumber;
        return this;
    }

    public void setSegmentNumber(Integer segmentNumber) {
        this.segmentNumber = segmentNumber;
    }

    public Integer getPrice() {
        return price;
    }

    public Fare price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Booking getBooking() {
        return booking;
    }

    public Fare booking(Booking booking) {
        this.booking = booking;
        return this;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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
        Fare fare = (Fare) o;
        if (fare.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fare.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Fare{" +
            "id=" + getId() +
            ", segmentNumber=" + getSegmentNumber() +
            ", price=" + getPrice() +
            "}";
    }
}
