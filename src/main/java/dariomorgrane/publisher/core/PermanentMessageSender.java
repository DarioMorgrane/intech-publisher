package dariomorgrane.publisher.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PermanentMessageSender implements MessageSender {

    private static final Logger LOG = LogManager.getLogger(PermanentMessageSender.class);
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    @Value("${subscriberURL}")
    private String subscriberURL;

    @Autowired
    public PermanentMessageSender(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    @Override
    public void send(String message) {
        LOG.info("Sending JSON message: " + message);
        restTemplate.postForEntity(subscriberURL, new HttpEntity<>(message, httpHeaders), String.class);
    }

}
