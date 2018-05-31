package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.CompanyClasse;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CompanyClasse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyClasseRepository extends JpaRepository<CompanyClasse, Long> {

}
