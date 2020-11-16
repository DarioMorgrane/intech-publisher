package dariomorgrane.publisher.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dariomorgrane.publisher.models.Action;
import dariomorgrane.publisher.models.Message;
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
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;
    private Random random;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setRandom(Random random) {
        this.random = random;
    }

    @PostConstruct
    private void setupCurrentId() {
        currentId = restTemplate.getForObject(subscriberURL, Long.class);
        LOG.info("Received last used ID in DB - " + currentId + " (0 means DB is empty)");
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

}
