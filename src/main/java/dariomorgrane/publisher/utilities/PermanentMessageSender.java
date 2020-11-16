package dariomorgrane.publisher.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class PermanentMessageSender implements MessageSender {

    private static final Logger LOG = LogManager.getLogger(PermanentMessageSender.class);
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;

    @Value("${subscriberURL}")
    private String subscriberURL;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    @PostConstruct
    private void setupHeaders() {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void send(String message) {
        LOG.info("Sending JSON message: " + message);
        restTemplate.postForEntity(subscriberURL, new HttpEntity<>(message, httpHeaders), String.class);
    }

}
