package rest_passport.service;

import org.springframework.stereotype.Service;
import rest_passport.model.Passport;
import rest_passport.repository.PassportRepository;

import java.util.*;

@Service
public class PassportService {
    private final PassportRepository repository;

    public PassportService(PassportRepository repository) {
        this.repository = repository;
    }

    public List<Passport> getAllPassports() {
        return repository.findAll();
    }

    public Passport savePassport(Passport passport) {
        return repository.save(passport);
    }

    public Optional<Passport> findPassportById(int id) {
        return repository.findById(id);
    }

    public void deletePassport(Passport passport) {
        repository.delete(passport);
    }

    public List<Passport> findPassportsBySeries(String series) {
        return repository.findBySeries(series);
    }

    public List<Passport> findExpiredPassports() {
        return repository.findAllExpiredPassports();
    }

    public List<Passport> findPassportsExpiredInThreeMonths() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);
        return repository.findReplaceablePassports(cal.getTime());
    }
}
