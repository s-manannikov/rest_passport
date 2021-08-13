package rest_passport.service;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rest_passport.model.Passport;

import java.util.List;

@Service
@AllArgsConstructor
public class PassportKafkaService {
    private final KafkaTemplate<Integer, Passport> kafkaTemplate;
    private final PassportAPIService passportAPIService;

    @Scheduled(fixedRate = 10000)
    public void checkExpiredPassports() {
        List<Passport> expiredPassports = passportAPIService.findExpiredPassports();
        if (expiredPassports.size() > 0) {
            expiredPassports.forEach(p -> kafkaTemplate.send("passports", p));
        } else {
            System.out.println("No expired passports!");
        }
    }

    @KafkaListener(topics = "passports")
    public void printMessage(ConsumerRecord<Integer, Passport> input) {
        System.out.println(input.value());
    }
}
