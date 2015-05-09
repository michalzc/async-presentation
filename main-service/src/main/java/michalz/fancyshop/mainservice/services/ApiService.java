package michalz.fancyshop.mainservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * Created by michal on 09.05.15.
 */
public class ApiService<T> {

    private Class<T> entityClass;
    private String urlPattern;

    @Autowired
    protected RestTemplate restTemplate;

    public ApiService(Class<T> entityClass, String urlPattern) {
        this.entityClass = entityClass;
        this.urlPattern = urlPattern;
    }

    public T getItem(Object params) {
        return restTemplate.getForObject(urlPattern, entityClass, params);
    }
}
