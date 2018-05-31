package ma.maman.jeanne.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FeesAgency.
 */
@Entity
@Table(name = "fees_agency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "feesagency")
public class FeesAgency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fees")
    private Integer fees;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Agency agency;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFees() {
        return fees;
    }

    public FeesAgency fees(Integer fees) {
        this.fees = fees;
        return this;
    }

    public void setFees(Integer fees) {
        this.fees = fees;
    }

    public Company getCompany() {
        return company;
    }

    public FeesAgency company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Agency getAgency() {
        return agency;
    }

    public FeesAgency agency(Agency agency) {
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
        FeesAgency feesAgency = (FeesAgency) o;
        if (feesAgency.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feesAgency.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FeesAgency{" +
            "id=" + getId() +
            ", fees=" + getFees() +
            "}";
    }
}
