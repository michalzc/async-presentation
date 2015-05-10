package michalz.fancyshop.mainservice.services;

import akka.dispatch.Futures;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import scala.concurrent.Future;
import scala.concurrent.Promise;


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

    public Future<T> getItem(Object param) {
        log.info("Getting {} for {}", entityClass.getName(), param);
        ListenableFuture<ResponseEntity<T>> listenableFuture = asyncRestTemplate.getForEntity(urlPattern, entityClass, param);
        return toScalaFuture(listenableFuture);
    }


    private <R> Future<R> toScalaFuture(ListenableFuture<ResponseEntity<R>> listenableFuture) {
        Promise<R> promise = Futures.promise();
        listenableFuture.addCallback(result -> promise.trySuccess(result.getBody()), ex -> promise.tryFailure(ex));
        return promise.future();
    }


}
