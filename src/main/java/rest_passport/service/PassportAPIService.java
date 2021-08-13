package rest_passport.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rest_passport.model.Passport;

import java.util.Collections;
import java.util.List;

@Service
public class PassportAPIService {
    @Value("${api-url}")
    private String url;

    private final RestTemplate client;

    public PassportAPIService(RestTemplateBuilder builder) {
        this.client = builder.build();
    }

    public List<Passport> getAllPassports() {
        return getList(url + "/");
    }

    public Passport savePassport(Passport passport) {
        return client.postForEntity(
                url, passport, Passport.class
        ).getBody();
    }

    public boolean update(String id, Passport passport) {
        return client.exchange(
                String.format("%s/update?id=%s", url, id),
                HttpMethod.PUT,
                new HttpEntity<>(passport),
                Void.class
        ).getStatusCode() != HttpStatus.NOT_FOUND;
    }

    public Passport findPassportById(int id) {
        return client.getForEntity(
                String.format("%s/getById?id=%s", url, id),
                Passport.class
        ).getBody();
    }

    public boolean deletePassport(int id, Passport passport) {
        return client.exchange(
                String.format("%s/delete?id=%s", url, id),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        ).getStatusCode() != HttpStatus.NOT_FOUND;
    }

    public List<Passport> findPassportsBySeries(String series) {
        return getList(String.format("%s/find?series=%s", url, series));
    }

    public List<Passport> findExpiredPassports() {
        return getList(String.format("%s/expired", url));
    }

    public List<Passport> findPassportsExpiredInThreeMonths() {
        return getList(String.format("%s/find-replaceable", url));
    }

    private List<Passport> getList(String url) {
        List<Passport> body = client.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Passport>>() {}
        ).getBody();
        return body == null ? Collections.emptyList() : body;
    }
}
