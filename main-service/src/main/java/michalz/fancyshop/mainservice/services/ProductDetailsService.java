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

        productInfoFuture.flatMap(new Mapper<ProductInfo, Future<ProductReviews>>() {
            @Override
            public Future<ProductReviews> apply(ProductInfo parameter) {
                productDetails.setProductInfo(parameter);
                log.info("Product info available");
                return productReviewsFuture;
            }
        }, actorSystem.dispatcher()).flatMap(new Mapper<ProductReviews, Future<ProductSuggestions>>() {
            @Override
            public Future<ProductSuggestions> apply(ProductReviews parameter) {
                productDetails.setProductReviews(parameter);
                log.info("Product reviews available");
                return productSuggestionsFuture;
            }
        }, actorSystem.dispatcher()).onComplete(new OnComplete<ProductSuggestions>() {
            @Override
            public void onComplete(Throwable failure, ProductSuggestions success) throws Throwable {
                if(failure != null) {
                    log.warn("Can't apply all futures", failure);
                } else {
                    log.info("All futures completed");
                    productDetails.setProductSuggestions(success);
                    result.setResult(productDetails);
                }
            }
        }, actorSystem.dispatcher());
    }
}
