package michalz.fancyshop.mainservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * Created by michal on 09.05.15.
 */
@Slf4j
public class ApiService<T> {

    private Class<T> entityClass;
    private String urlPattern;

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    public ApiService(Class<T> entityClass, String urlPattern) {
        this.entityClass = entityClass;
        this.urlPattern = urlPattern;
    }

    public ListenableFuture<ResponseEntity<T>> getItem(Object param) {
        log.info("Getting {} for {}", entityClass.getName(), param);
        return asyncRestTemplate.getForEntity(urlPattern, entityClass, param);
    }


}
