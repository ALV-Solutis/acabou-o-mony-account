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

    private final UserService userService;

    public SecurityProducer(RabbitTemplate rabbitTemplate, UserService userService) {
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
    }

    @Value(value = "${broker.queue.email.name.authentication}")
    private String routingKey;

    public void publishMessageEmail(MultiFactorAuth mfa) {
        String email = userService.getEmailByUserId(mfa.getUserId());
        EmailDTO emailDto = new EmailDTO(mfa.getUserId(), email, mfa.getCode());

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }
}
