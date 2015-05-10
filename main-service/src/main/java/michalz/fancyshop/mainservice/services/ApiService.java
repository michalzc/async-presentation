package michalz.fancyshop.mainservice.services;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.pattern.Patterns;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by michal on 09.05.15.
 */
@Slf4j
public class ApiService<T> {

    private Class<T> entityClass;
    private String urlPattern;

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @Autowired
    private ActorSystem actorSystem;

    public ApiService(Class<T> entityClass, String urlPattern) {
        this.entityClass = entityClass;
        this.urlPattern = urlPattern;
    }

    public Future<T> getItem(Object param, Long timeout) {
        log.info("Getting {} for {}", entityClass.getName(), param);
        ListenableFuture<ResponseEntity<T>> listenableFuture = asyncRestTemplate.getForEntity(urlPattern, entityClass, param);
        return enhanceFuture(listenableFuture, timeout);
    }


    private <R> Future<R> enhanceFuture(ListenableFuture<ResponseEntity<R>> listenableFuture, Long timeout) {
        Promise<R> promise = Futures.promise();
        listenableFuture.addCallback(result -> promise.trySuccess(result.getBody()), ex -> promise.tryFailure(ex));
        Future<R> failed = Patterns.after(Duration.create(timeout, TimeUnit.MILLISECONDS), actorSystem.scheduler(),
                actorSystem.dispatcher(), Futures.failed(new TimeoutException("Request timed out")));

        return Futures.firstCompletedOf(Arrays.asList(promise.future(), failed), actorSystem.dispatcher());
    }


}
