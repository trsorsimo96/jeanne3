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
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "booking")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "pnr", nullable = false)
    private String pnr;

    @NotNull
    @Column(name = "booking_date", nullable = false)
    private ZonedDateTime bookingDate;

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Passenger> passengers = new HashSet<>();

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fare> fares = new HashSet<>();

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Email> emails = new HashSet<>();

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Segment> segments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPnr() {
        return pnr;
    }

    public Booking pnr(String pnr) {
        this.pnr = pnr;
        return this;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public ZonedDateTime getBookingDate() {
        return bookingDate;
    }

    public Booking bookingDate(ZonedDateTime bookingDate) {
        this.bookingDate = bookingDate;
        return this;
    }

    public void setBookingDate(ZonedDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public Booking passengers(Set<Passenger> passengers) {
        this.passengers = passengers;
        return this;
    }

    public Booking addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.setBooking(this);
        return this;
    }

    public Booking removePassenger(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.setBooking(null);
        return this;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Set<Fare> getFares() {
        return fares;
    }

    public Booking fares(Set<Fare> fares) {
        this.fares = fares;
        return this;
    }

    public Booking addFare(Fare fare) {
        this.fares.add(fare);
        fare.setBooking(this);
        return this;
    }

    public Booking removeFare(Fare fare) {
        this.fares.remove(fare);
        fare.setBooking(null);
        return this;
    }

    public void setFares(Set<Fare> fares) {
        this.fares = fares;
    }

    public Set<Email> getEmails() {
        return emails;
    }

    public Booking emails(Set<Email> emails) {
        this.emails = emails;
        return this;
    }

    public Booking addEmail(Email email) {
        this.emails.add(email);
        email.setBooking(this);
        return this;
    }

    public Booking removeEmail(Email email) {
        this.emails.remove(email);
        email.setBooking(null);
        return this;
    }

    public void setEmails(Set<Email> emails) {
        this.emails = emails;
    }

    public Set<Segment> getSegments() {
        return segments;
    }

    public Booking segments(Set<Segment> segments) {
        this.segments = segments;
        return this;
    }

    public Booking addSegment(Segment segment) {
        this.segments.add(segment);
        segment.setBooking(this);
        return this;
    }

    public Booking removeSegment(Segment segment) {
        this.segments.remove(segment);
        segment.setBooking(null);
        return this;
    }

    public void setSegments(Set<Segment> segments) {
        this.segments = segments;
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
        Booking booking = (Booking) o;
        if (booking.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), booking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", pnr='" + getPnr() + "'" +
            ", bookingDate='" + getBookingDate() + "'" +
            "}";
    }
}
