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
 * A Classe.
 */
@Entity
@Table(name = "classe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "classe")
public class Classe implements Serializable {

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

    @OneToMany(mappedBy = "classe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Itineraire> itineraires = new HashSet<>();

    @OneToMany(mappedBy = "classe")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CompanyClasse> companyClasses = new HashSet<>();

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

    public Classe code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Classe name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Itineraire> getItineraires() {
        return itineraires;
    }

    public Classe itineraires(Set<Itineraire> itineraires) {
        this.itineraires = itineraires;
        return this;
    }

    public Classe addItineraire(Itineraire itineraire) {
        this.itineraires.add(itineraire);
        itineraire.setClasse(this);
        return this;
    }

    public Classe removeItineraire(Itineraire itineraire) {
        this.itineraires.remove(itineraire);
        itineraire.setClasse(null);
        return this;
    }

    public void setItineraires(Set<Itineraire> itineraires) {
        this.itineraires = itineraires;
    }

    public Set<CompanyClasse> getCompanyClasses() {
        return companyClasses;
    }

    public Classe companyClasses(Set<CompanyClasse> companyClasses) {
        this.companyClasses = companyClasses;
        return this;
    }

    public Classe addCompanyClasse(CompanyClasse companyClasse) {
        this.companyClasses.add(companyClasse);
        companyClasse.setClasse(this);
        return this;
    }

    public Classe removeCompanyClasse(CompanyClasse companyClasse) {
        this.companyClasses.remove(companyClasse);
        companyClasse.setClasse(null);
        return this;
    }

    public void setCompanyClasses(Set<CompanyClasse> companyClasses) {
        this.companyClasses = companyClasses;
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
        Classe classe = (Classe) o;
        if (classe.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classe.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Classe{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
