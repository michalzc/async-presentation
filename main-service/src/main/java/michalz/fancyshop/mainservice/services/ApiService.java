package michalz.fancyshop.mainservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * Created by michal on 09.05.15.
 */
@Slf4j
public class ApiService<T> {

    private Class<T> entityClass;
    private String urlPattern;

    @Autowired
    protected RestTemplate restTemplate;

    public ApiService(Class<T> entityClass, String urlPattern) {
        this.entityClass = entityClass;
        this.urlPattern = urlPattern;
    }

    @Async
    public Future<T> getItem(Object params) {
        log.info("Getting {} for {}", entityClass.getName(), params);
        return new AsyncResult<>(restTemplate.getForObject(urlPattern, entityClass, params));
    }
}
