package michalz.fancyshop.mainservice.services;

import michalz.fancyshop.dto.ProductDetails;
import michalz.fancyshop.dto.ProductInfo;
import michalz.fancyshop.dto.ProductReviews;
import michalz.fancyshop.dto.ProductSuggestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by michal on 09.05.15.
 */
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

}
