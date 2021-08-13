package rest_passport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rest_passport.model.Passport;

import java.util.Date;
import java.util.List;

public interface PassportRepository extends JpaRepository<Passport, Integer> {

    List<Passport> findBySeries(String series);

    @Query("select p from Passport p where p.expiryDate < current_date")
    List<Passport> findAllExpiredPassports();

    @Query("select p from Passport p where p.expiryDate between current_date and :endDate")
    List<Passport> findReplaceablePassports(@Param("endDate")Date endDate);
}
