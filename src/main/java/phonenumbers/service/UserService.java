package phonenumbers.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.WebServiceIOException;
import phonenumbers.dto.PhonesResponseDto;
import phonenumbers.dto.UserResultDto;
import phonenumbers.service.client.PhoneNumberClient;
import phonenumbers.service.client.UserClientWS;
import phonenumbers.webservice.GetUserResponse;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Сервис для получения данных пользователя
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserClientWS userClientWS;
    private final PhoneNumberClient phoneNumberClient;

    /**
     * Получает данные пользователя по id из других сервисов и суммирует результаты.
     *
     * @param id идентификатор пользователя
     * @return результат с данными пользователя или кодом ошибки
     */
    public UserResultDto mergeUserData(int id) {

        UserResultDto resultDto = new UserResultDto();

        CompletableFuture<Void> userFuture = CompletableFuture.runAsync(() -> {
            GetUserResponse userResponse = userClientWS.getUserResponse(id);
            resultDto.setCode(0);
            resultDto.setName(String.format("%s %s",
                    userResponse.getUser().getFirstName(),
                    userResponse.getUser().getLastName()));
        }).exceptionally(ex -> {
                    if (ex.getCause() != null && ex.getCause().getClass() == WebServiceIOException.class &&
                            ex.getCause().getCause() != null &&
                            ex.getCause().getCause().getClass() == SocketTimeoutException.class) {
                        log.error("Timeout exception по исключению сервиса: {} {}", id, ex.getMessage());
                        resultDto.setCode(1);
                    } else {
                        log.error("Ошибка при получении данных пользователя: {} {}", id, ex.getMessage());
                        resultDto.setCode(2);
                    }
                    return null;
                }
        );
        CompletableFuture<String> phonesFuture = CompletableFuture.supplyAsync(
                () -> {
                    PhonesResponseDto userPhones = phoneNumberClient.getUserPhones(id);
                    List<String> phones = userPhones.getPhones();
                    if (phones != null && !phones.isEmpty()) {
                        return phones.get(0);
                    }
                    return null;
                }
        ).exceptionally(ex -> {
            log.info("Ошибка РЕСТ: {} {}", id, ex.getMessage());
            return null;
        });

        try {
            CompletableFuture.allOf(userFuture, phonesFuture).get();
        } catch (
                Exception e) {
            log.error("Что-то пошло не так.. {}", e.getMessage());
            resultDto.setCode(2);
        }
        if (resultDto.getCode() == 0) {
            resultDto.setPhone(phonesFuture.getNow(null));
        }
        log.info("{}", resultDto);
        return resultDto;
    }
}
