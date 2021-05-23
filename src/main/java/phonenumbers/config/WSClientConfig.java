package phonenumbers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import phonenumbers.service.client.UserClientWS;

@Configuration
public class WSClientConfig {

    @Value("${ws.endpoint}")
    private String wsEndpoint;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("phonenumbers.webservice");
        return marshaller;
    }
    @Bean
    public UserClientWS userClientWS(Jaxb2Marshaller marshaller) {
        UserClientWS client = new UserClientWS();
        client.setDefaultUri(wsEndpoint);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageSender(httpComponentsMessageSender());
        return client;
    }

    @Bean
    public HttpComponentsMessageSender httpComponentsMessageSender() {
        HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
        sender.setReadTimeout(5000);
        sender.setConnectionTimeout(5000);
        return sender;
    }
}
