package michalz.fancyshop.mainservice.services;

import lombok.extern.slf4j.Slf4j;
import michalz.fancyshop.dto.ProductDetails;
import michalz.fancyshop.dto.ProductInfo;
import michalz.fancyshop.dto.ProductReviews;
import michalz.fancyshop.dto.ProductSuggestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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


    public ProductDetails getProductDetails(Long productId) throws ExecutionException, InterruptedException {
        ProductDetails productDetails = new ProductDetails();

        Future<ProductInfo> productInfoFuture = productInfoApiService.getItem(productId);
        Future<ProductReviews> productReviewsFuture = productReviewsApiService.getItem(productId);
        Future<ProductSuggestions> productSuggestionsFuture = productSuggestionsApiService.getItem(productId);

        productDetails.setProductInfo(productInfoFuture.get());
        productDetails.setProductReviews(productReviewsFuture.get());
        productDetails.setProductSuggestions(productSuggestionsFuture.get());

        return productDetails;
    }

    @Async
    public void productDetails(Long productId, DeferredResult<ProductDetails> result) throws ExecutionException, InterruptedException {
        log.info("Getting product details for {}", productId);
        ProductDetails productDetails = getProductDetails(productId);
        result.setResult(productDetails);
        log.info("Result updated");
    }

}
