package phonenumbers.service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import phonenumbers.webservice.GetUserRequest;
import phonenumbers.webservice.GetUserResponse;

/**
 * Клиент для получения данных пользователя по SOAP
 */
@Slf4j
public class UserClientWS extends WebServiceGatewaySupport {

    /**
     * Получает данные пользователя по id из стороннего сервиса по SOAP
     *
     * @param id идентификатор пользователя
     * @return результат запроса с данными пользователя
     */
    public GetUserResponse getUserResponse(int id) {
        GetUserRequest request = new GetUserRequest();
        request.setUserId(id);

        log.info("Попытка получить данные по WS...");
        GetUserResponse response = (GetUserResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);

        return response;
    }
}
