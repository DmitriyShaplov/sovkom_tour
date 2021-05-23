package phonenumbers.service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import phonenumbers.dto.PhonesResponseDto;

import java.net.URI;

/**
 * Клиент для получения списка телефонов пользователя.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneNumberClient {

    private final RestTemplate restTemplate;

    @Value("${rs.endpoint}")
    private String rsEndpoint;

    public PhonesResponseDto getUserPhones(int id) {

        URI uri = UriComponentsBuilder.fromHttpUrl(rsEndpoint)
                .path("api/v1/phones")
                .pathSegment(String.valueOf(id)).build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        log.info("Пытаемся получить данные по РЕСТ...");
        ResponseEntity<PhonesResponseDto> forEntity = restTemplate
                .exchange(uri, HttpMethod.GET, entity, PhonesResponseDto.class);
        return forEntity.getBody();
    }

}
