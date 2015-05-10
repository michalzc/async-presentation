package michalz.fancyshop.mainservice.services;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.dispatch.OnComplete;
import akka.dispatch.Recover;
import lombok.extern.slf4j.Slf4j;
import michalz.fancyshop.dto.ProductDetails;
import michalz.fancyshop.dto.ProductInfo;
import michalz.fancyshop.dto.ProductReviews;
import michalz.fancyshop.dto.ProductSuggestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.async.DeferredResult;
import scala.concurrent.Future;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by michal on 09.05.15.
 */
@Slf4j
public class ProductDetailsService {

    @Value("${rest.external.timeout}")
    private Long externalTimeout;

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

    public void productDetails(Long productId, DeferredResult<ProductDetails> result) throws ExecutionException,
            InterruptedException {
        log.info("Getting product details for {}", productId);

        ProductDetails productDetails = new ProductDetails();

        Future<ProductInfo> productInfoFuture = productInfoApiService.getItem(productId, externalTimeout).recover(new Recover<ProductInfo>() {
            @Override
            public ProductInfo recover(Throwable failure) throws Throwable {
                log.warn("Can't get product info", failure);
                return new ProductInfo();
            }
        }, actorSystem.dispatcher());

        Future<ProductReviews> productReviewsFuture = productReviewsApiService.getItem(productId, externalTimeout)
                .recover(new Recover<ProductReviews>() {
            @Override
            public ProductReviews recover(Throwable failure) throws Throwable {
                log.warn("Can't get product reviews", failure);
                return new ProductReviews();
            }

        }, actorSystem.dispatcher());

        Future<ProductSuggestions> productSuggestionsFuture = productSuggestionsApiService.getItem(productId,
                externalTimeout).recover(new Recover<ProductSuggestions>() {
            @Override
            public ProductSuggestions recover(Throwable failure) throws Throwable {
                log.warn("Can't get product suggestions", failure);
                return new ProductSuggestions();
            }
        }, actorSystem.dispatcher());

        Future<Iterable<Object>> allItemsFuture = Futures.sequence(Arrays.asList((Future)productInfoFuture,
                (Future)productReviewsFuture, (Future)productSuggestionsFuture), actorSystem.dispatcher());

        allItemsFuture.onComplete(new OnComplete<Iterable<Object>>() {
            @Override
            public void onComplete(Throwable failure, Iterable<Object> success) throws Throwable {

                if (failure == null) {
                    success.forEach(item -> {
                        if (item instanceof ProductInfo) {
                            productDetails.setProductInfo((ProductInfo) item);
                        } else if (item instanceof ProductSuggestions) {
                            productDetails.setProductSuggestions((ProductSuggestions) item);
                        } else if (item instanceof ProductReviews) {
                            productDetails.setProductReviews((ProductReviews) item);
                        }
                    });
                    result.setResult(productDetails);
                } else {
                    log.warn("Can't get response", failure);
                    result.setErrorResult(failure);
                }
            }
        }, actorSystem.dispatcher());
    }
}
