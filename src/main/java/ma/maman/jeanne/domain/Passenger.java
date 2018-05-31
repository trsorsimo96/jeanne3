package ma.maman.jeanne.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import ma.maman.jeanne.domain.enumeration.TypePassenger;

import ma.maman.jeanne.domain.enumeration.TitlePassenger;

/**
 * A Passenger.
 */
@Entity
@Table(name = "passenger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "passenger")
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "cni")
    private String cni;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private TypePassenger type;

    @Enumerated(EnumType.STRING)
    @Column(name = "title")
    private TitlePassenger title;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "seat_number")
    private Integer seatNumber;

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

    public String getName() {
        return name;
    }

    public Passenger name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public Passenger surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCni() {
        return cni;
    }

    public Passenger cni(String cni) {
        this.cni = cni;
        return this;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public TypePassenger getType() {
        return type;
    }

    public Passenger type(TypePassenger type) {
        this.type = type;
        return this;
    }

    public void setType(TypePassenger type) {
        this.type = type;
    }

    public TitlePassenger getTitle() {
        return title;
    }

    public Passenger title(TitlePassenger title) {
        this.title = title;
        return this;
    }

    public void setTitle(TitlePassenger title) {
        this.title = title;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public Passenger ticketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
        return this;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public Passenger seatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
        return this;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Booking getBooking() {
        return booking;
    }

    public Passenger booking(Booking booking) {
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
        Passenger passenger = (Passenger) o;
        if (passenger.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), passenger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Passenger{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", cni='" + getCni() + "'" +
            ", type='" + getType() + "'" +
            ", title='" + getTitle() + "'" +
            ", ticketNumber='" + getTicketNumber() + "'" +
            ", seatNumber=" + getSeatNumber() +
            "}";
    }
}
