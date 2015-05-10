package michalz.fancyshop.mainservice.services;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.dispatch.OnComplete;
import lombok.extern.slf4j.Slf4j;
import michalz.fancyshop.dto.ProductDetails;
import michalz.fancyshop.dto.ProductInfo;
import michalz.fancyshop.dto.ProductReviews;
import michalz.fancyshop.dto.ProductSuggestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import scala.concurrent.Future;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by michal on 09.05.15.
 */
@Slf4j
public class ProductDetailsService {

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    @Qualifier("productInfoApiService")
    private ApiService<ProductInfo> productInfoApiService;

    @Autowired
    @Qualifier("productSuggestionsApiService")
    private ApiService<ProductSuggestions> productSuggestionsApiService;

    @Autowired
    @Qualifier("productReviewsApiService")
    private ApiService<ProductReviews> productReviewsApiService;

    public void productDetails(Long productId, DeferredResult<ProductDetails> result) throws ExecutionException, InterruptedException {
        log.info("Getting product details for {}", productId);

        ProductDetails productDetails = new ProductDetails();

        Future<ResponseEntity<ProductInfo>> infoFuture = productInfoApiService.getItem(productId);
        Future<ResponseEntity<ProductReviews>> reviewsFuture = productReviewsApiService.getItem(productId);
        Future<ResponseEntity<ProductSuggestions>> suggestionsFuture = productSuggestionsApiService.getItem(productId);

        infoFuture.onComplete(new OnComplete<ResponseEntity<ProductInfo>>() {
            @Override
            public void onComplete(Throwable failure, ResponseEntity<ProductInfo> success) throws Throwable {
                if (failure != null) {
                    log.warn("Can't get product info", failure);
                } else {
                    log.info("Product info ");
                    productDetails.setProductInfo(success.getBody());
                }
            }
        }, actorSystem.dispatcher());

        reviewsFuture.onComplete(new OnComplete<ResponseEntity<ProductReviews>>() {
            @Override
            public void onComplete(Throwable failure, ResponseEntity<ProductReviews> success) throws Throwable {
                if (failure != null) {
                    log.warn("Can't get reviews", failure);
                } else {
                    log.info("Reviews received");
                    productDetails.setProductReviews(success.getBody());
                }
            }
        }, actorSystem.dispatcher());

        suggestionsFuture.onComplete(new OnComplete<ResponseEntity<ProductSuggestions>>() {
            @Override
            public void onComplete(Throwable failure, ResponseEntity<ProductSuggestions> success) throws Throwable {
                if (failure != null) {
                    log.warn("Can't get suggestions", failure);
                } else {
                    log.info("Suggestions received");
                    productDetails.setProductSuggestions(success.getBody());
                }
            }
        }, actorSystem.dispatcher());
        

        Future<Iterable<Object>> seqFuture = Futures.sequence(
                Arrays.asList(mapToObjectFuture(infoFuture), mapToObjectFuture(reviewsFuture),
                        mapToObjectFuture(suggestionsFuture)), actorSystem.dispatcher());

        seqFuture.onComplete(new OnComplete<Iterable<Object>>() {
            @Override
            public void onComplete(Throwable failure, Iterable<Object> success) throws Throwable {
                if(failure != null) {
                    log.warn("At last one future failed", failure);
                } else {
                    log.info("All futures complete");
                }
                result.setResult(productDetails);
            }
        }, actorSystem.dispatcher());
    }

    private <T> Future<Object> mapToObjectFuture(Future<T> inputFuture) {
        return inputFuture.map(new Mapper<T, Object>() {
            public Object apply(T input) {
                return input;
            }
        }, actorSystem.dispatcher());
    }


}
