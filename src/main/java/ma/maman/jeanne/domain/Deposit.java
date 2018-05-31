package ma.maman.jeanne.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import ma.maman.jeanne.domain.enumeration.ModePayment;

/**
 * A Deposit.
 */
@Entity
@Table(name = "deposit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "deposit")
public class Deposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_payment", nullable = false)
    private ModePayment modePayment;

    @ManyToOne(optional = false)
    @NotNull
    private Agency agency;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public Deposit amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Deposit date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ModePayment getModePayment() {
        return modePayment;
    }

    public Deposit modePayment(ModePayment modePayment) {
        this.modePayment = modePayment;
        return this;
    }

    public void setModePayment(ModePayment modePayment) {
        this.modePayment = modePayment;
    }

    public Agency getAgency() {
        return agency;
    }

    public Deposit agency(Agency agency) {
        this.agency = agency;
        return this;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
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
        Deposit deposit = (Deposit) o;
        if (deposit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deposit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Deposit{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            ", modePayment='" + getModePayment() + "'" +
            "}";
    }
}
