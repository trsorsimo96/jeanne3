package ma.maman.jeanne.repository;

import ma.maman.jeanne.domain.Deposit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Deposit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

}
