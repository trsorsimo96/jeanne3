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

import ma.maman.jeanne.domain.enumeration.TypeCompany;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "code", length = 3, nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @Column(name = "tel")
    private String tel;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "om_number")
    private String omNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private TypeCompany type;

    @Column(name = "momo_number")
    private String momoNumber;

    @NotNull
    @Column(name = "system_commision", nullable = false)
    private Long systemCommision;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Car> cars = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Train> trains = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Itineraire> itineraires = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CompanyClasse> companyClasses = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FeesAgency> feesAgencies = new HashSet<>();

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

    public Company code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public Company image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Company imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getTel() {
        return tel;
    }

    public Company tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public Company email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOmNumber() {
        return omNumber;
    }

    public Company omNumber(String omNumber) {
        this.omNumber = omNumber;
        return this;
    }

    public void setOmNumber(String omNumber) {
        this.omNumber = omNumber;
    }

    public TypeCompany getType() {
        return type;
    }

    public Company type(TypeCompany type) {
        this.type = type;
        return this;
    }

    public void setType(TypeCompany type) {
        this.type = type;
    }

    public String getMomoNumber() {
        return momoNumber;
    }

    public Company momoNumber(String momoNumber) {
        this.momoNumber = momoNumber;
        return this;
    }

    public void setMomoNumber(String momoNumber) {
        this.momoNumber = momoNumber;
    }

    public Long getSystemCommision() {
        return systemCommision;
    }

    public Company systemCommision(Long systemCommision) {
        this.systemCommision = systemCommision;
        return this;
    }

    public void setSystemCommision(Long systemCommision) {
        this.systemCommision = systemCommision;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public Company cars(Set<Car> cars) {
        this.cars = cars;
        return this;
    }

    public Company addCar(Car car) {
        this.cars.add(car);
        car.setCompany(this);
        return this;
    }

    public Company removeCar(Car car) {
        this.cars.remove(car);
        car.setCompany(null);
        return this;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public Set<Train> getTrains() {
        return trains;
    }

    public Company trains(Set<Train> trains) {
        this.trains = trains;
        return this;
    }

    public Company addTrain(Train train) {
        this.trains.add(train);
        train.setCompany(this);
        return this;
    }

    public Company removeTrain(Train train) {
        this.trains.remove(train);
        train.setCompany(null);
        return this;
    }

    public void setTrains(Set<Train> trains) {
        this.trains = trains;
    }

    public Set<Itineraire> getItineraires() {
        return itineraires;
    }

    public Company itineraires(Set<Itineraire> itineraires) {
        this.itineraires = itineraires;
        return this;
    }

    public Company addItineraire(Itineraire itineraire) {
        this.itineraires.add(itineraire);
        itineraire.setCompany(this);
        return this;
    }

    public Company removeItineraire(Itineraire itineraire) {
        this.itineraires.remove(itineraire);
        itineraire.setCompany(null);
        return this;
    }

    public void setItineraires(Set<Itineraire> itineraires) {
        this.itineraires = itineraires;
    }

    public Set<CompanyClasse> getCompanyClasses() {
        return companyClasses;
    }

    public Company companyClasses(Set<CompanyClasse> companyClasses) {
        this.companyClasses = companyClasses;
        return this;
    }

    public Company addCompanyClasse(CompanyClasse companyClasse) {
        this.companyClasses.add(companyClasse);
        companyClasse.setCompany(this);
        return this;
    }

    public Company removeCompanyClasse(CompanyClasse companyClasse) {
        this.companyClasses.remove(companyClasse);
        companyClasse.setCompany(null);
        return this;
    }

    public void setCompanyClasses(Set<CompanyClasse> companyClasses) {
        this.companyClasses = companyClasses;
    }

    public Set<FeesAgency> getFeesAgencies() {
        return feesAgencies;
    }

    public Company feesAgencies(Set<FeesAgency> feesAgencies) {
        this.feesAgencies = feesAgencies;
        return this;
    }

    public Company addFeesAgency(FeesAgency feesAgency) {
        this.feesAgencies.add(feesAgency);
        feesAgency.setCompany(this);
        return this;
    }

    public Company removeFeesAgency(FeesAgency feesAgency) {
        this.feesAgencies.remove(feesAgency);
        feesAgency.setCompany(null);
        return this;
    }

    public void setFeesAgencies(Set<FeesAgency> feesAgencies) {
        this.feesAgencies = feesAgencies;
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
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", tel='" + getTel() + "'" +
            ", email='" + getEmail() + "'" +
            ", omNumber='" + getOmNumber() + "'" +
            ", type='" + getType() + "'" +
            ", momoNumber='" + getMomoNumber() + "'" +
            ", systemCommision=" + getSystemCommision() +
            "}";
    }
}
