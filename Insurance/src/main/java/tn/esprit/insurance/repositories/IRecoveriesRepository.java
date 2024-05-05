package tn.esprit.insurance.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tn.esprit.insurance.entities.Loan;
import tn.esprit.insurance.entities.Recoveries;

import java.util.List;

public interface IRecoveriesRepository extends CrudRepository<Recoveries,Integer> {
    @Query("SELECT MONTH(r.datesup) as month, "
            + "SUM(CASE WHEN r.daterel IS NOT NULL THEN r.amount ELSE 0 END) as sumWithDateRelNotNull, "
            + "SUM(CASE WHEN r.daterel IS NULL THEN r.amount ELSE 0 END) as sumWithDateRelNull "
            + "FROM Recoveries r "
            + "WHERE YEAR(r.datesup) = YEAR(CURRENT_DATE) "
            + "GROUP BY MONTH(r.datesup)")
    List<Object[]> getRecoverySumsForCurrentYear();

   /* @Query("SELECT SUM(CASE WHEN r.daterel IS NOT NULL AND r.daterel > r.datesup THEN (FUNCTION('DATEDIFF', 'DAY', r.datesup, r.daterel)) ELSE 0 END) "
            + "FROM Recoveries r "
            + "JOIN r.loan l "
            + "JOIN l.user u "
            + "WHERE u.Id = :userId "
            + "AND r.datesup < CURRENT_DATE")
    Integer getTotalRecoveryDaysDifferenceForCurrentUser(@Param("userId") int userId);*/
   @Query("SELECT SUM(CASE WHEN r.daterel IS NOT NULL AND r.daterel > r.datesup THEN (FUNCTION('DATEDIFF', r.daterel, r.datesup)) ELSE 0 END) "
           + "FROM Recoveries r "
           + "JOIN r.loan l "
           + "JOIN l.user u "
           + "WHERE u.Id = :userId "
           + "AND r.datesup < CURRENT_DATE")
   Integer getTotalRecoveryDaysDifferenceForCurrentUser(@Param("userId") int userId);
   List<Recoveries> findByLoan(Loan loan);

}
