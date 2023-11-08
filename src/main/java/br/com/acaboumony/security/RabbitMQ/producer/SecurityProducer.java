package br.com.acaboumony.security.RabbitMQ.producer;

import br.com.acaboumony.account.dto.response.EmailDTO;
import br.com.acaboumony.account.service.UserService;
import br.com.acaboumony.security.model.MultiFactorAuth;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityProducer {

    private final RabbitTemplate rabbitTemplate;


    public SecurityProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name.authentication}")
    private String routingKey;

    public void publishMessageEmail(MultiFactorAuth mfa) {
        EmailDTO emailDto = new EmailDTO(mfa.getUserId(), mfa.getEmail(), mfa.getCode());

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }
}
