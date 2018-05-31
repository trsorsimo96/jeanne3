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
 * A City.
 */
@Entity
@Table(name = "city")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "city")
public class City implements Serializable {

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

    @OneToMany(mappedBy = "origin")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Routes> routes = new HashSet<>();

    @OneToMany(mappedBy = "destination")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Routes> routes = new HashSet<>();

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

    public City code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public City name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Routes> getRoutes() {
        return routes;
    }

    public City routes(Set<Routes> routes) {
        this.routes = routes;
        return this;
    }

    public City addRoutes(Routes routes) {
        this.routes.add(routes);
        routes.setOrigin(this);
        return this;
    }

    public City removeRoutes(Routes routes) {
        this.routes.remove(routes);
        routes.setOrigin(null);
        return this;
    }

    public void setRoutes(Set<Routes> routes) {
        this.routes = routes;
    }

    public Set<Routes> getRoutes() {
        return routes;
    }

    public City routes(Set<Routes> routes) {
        this.routes = routes;
        return this;
    }

    public City addRoutes(Routes routes) {
        this.routes.add(routes);
        routes.setDestination(this);
        return this;
    }

    public City removeRoutes(Routes routes) {
        this.routes.remove(routes);
        routes.setDestination(null);
        return this;
    }

    public void setRoutes(Set<Routes> routes) {
        this.routes = routes;
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
        City city = (City) o;
        if (city.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), city.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "City{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
