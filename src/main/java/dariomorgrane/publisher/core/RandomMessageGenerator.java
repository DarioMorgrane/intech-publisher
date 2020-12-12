package dariomorgrane.publisher.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dariomorgrane.publisher.model.Action;
import dariomorgrane.publisher.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Random;

@Component
public class RandomMessageGenerator implements MessageGenerator {

    @Value("${subscriberURL}")
    private String subscriberURL;
    private static final Logger LOG = LogManager.getLogger(RandomMessageGenerator.class);
    private Long currentId;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final Random random;

    @Autowired
    public RandomMessageGenerator(ObjectMapper objectMapper, RestTemplate restTemplate, Random random) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.random = random;
    }

    public String generateMessage() throws JsonProcessingException {
        currentId++;
        Message message = new Message();
        message.setId(currentId);
        message.setMsisdn(random.nextInt(123456790));
        message.setAction(Action.values()[random.nextInt(Action.values().length)]);
        message.setTimestamp(System.currentTimeMillis());
        LOG.info("Created object: " + message);
        return objectMapper.writeValueAsString(message);
    }

    @PostConstruct
    private void setupCurrentId() {
        currentId = restTemplate.getForObject(subscriberURL, Long.class);
        LOG.info("Received last used ID in DB table - " + currentId + " (0 means DB table is empty)");
    }

}
