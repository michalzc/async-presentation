package michalz.fancyshop.mainservice.services;

import lombok.extern.slf4j.Slf4j;
import michalz.fancyshop.dto.ProductDetails;
import michalz.fancyshop.dto.ProductInfo;
import michalz.fancyshop.dto.ProductReviews;
import michalz.fancyshop.dto.ProductSuggestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutionException;

/**
 * Created by michal on 09.05.15.
 */
@Slf4j
public class ProductDetailsService {

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

        ListenableFuture<ResponseEntity<ProductInfo>> infoFuture = productInfoApiService.getItem(productId);
        ListenableFuture<ResponseEntity<ProductReviews>> reviewsFuture = productReviewsApiService.getItem(productId);
        ListenableFuture<ResponseEntity<ProductSuggestions>> sugesstionsFuture = productSuggestionsApiService.getItem(productId);

        infoFuture.addCallback(entity -> {
            log.info("Product info available");
            productDetails.setProductInfo(entity.getBody());
            tryComplete(result, productDetails);
        }, ex -> {
            log.warn("Can't get product info", ex);
            productDetails.setProductInfo(new ProductInfo());
            tryComplete(result, productDetails);
        });

        reviewsFuture.addCallback(entity -> {
            log.info("Product reviews available");
            productDetails.setProductReviews(entity.getBody());
            tryComplete(result, productDetails);
        }, ex -> {
            log.warn("Can't get product reviews", ex);
            productDetails.setProductReviews(new ProductReviews());
            tryComplete(result, productDetails);
        });

        sugesstionsFuture.addCallback(entity -> {
            log.info("Product sugesstions available");
            productDetails.setProductSuggestions(entity.getBody());
            tryComplete(result, productDetails);
        }, ex -> {
            log.warn("Can't get product sugesstions", ex);
            productDetails.setProductSuggestions(new ProductSuggestions());
            tryComplete(result, productDetails);
        });
    }

    private void tryComplete(DeferredResult<ProductDetails> result, ProductDetails productDetails) {
        if(productDetails.isComplete()) {
            result.setResult(productDetails);
            log.info("Request completed");
        }
    }

}
