package rest_passport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest_passport.model.Passport;
import rest_passport.service.PassportService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/passport")
public class PassportController {
    private final PassportService service;

    public PassportController(PassportService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Passport> findAll() {
        return service.getAllPassports();
    }

    @PostMapping("/save")
    public ResponseEntity<Passport> save(@RequestBody Passport passport) {
        return new ResponseEntity<>(service.savePassport(passport), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Passport> update(@RequestParam int id, @RequestBody Passport passport) {
        Optional<Passport> pass = service.findPassportById(id);
        pass.ifPresent(p -> {
            p.setNumber(passport.getNumber());
            p.setSeries(passport.getSeries());
            p.setExpiryDate(passport.getExpiryDate());
            service.savePassport(p);
        });
        return new ResponseEntity<>(pass.orElse(new Passport()),
                pass.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam int id) {
        Optional<Passport> pass = service.findPassportById(id);
        pass.ifPresent(service::deletePassport);
        return ResponseEntity.status(pass.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/find")
    public List<Passport> findBySeries(@RequestParam String series) {
        return service.findPassportsBySeries(series);
    }

    @GetMapping("/expired")
    public List<Passport> findExpired() {
        return service.findExpiredPassports();
    }

    @GetMapping("/find-replaceable")
    public List<Passport> findReplaceable() {
        return service.findPassportsExpiredInThreeMonths();
    }
}
