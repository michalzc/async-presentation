package michalz.fancyshop.mainservice.services;

import michalz.fancyshop.dto.ProductDetails;
import michalz.fancyshop.dto.ProductInfo;
import michalz.fancyshop.dto.ProductReviews;
import michalz.fancyshop.dto.ProductSuggestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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


    public ProductDetails getProductDetails(Long productId) {
        ProductDetails productDetails = new ProductDetails();

        productDetails.setProductInfo(productInfoApiService.getItem(productId));
        productDetails.setProductReviews(productReviewsApiService.getItem(productId));
        productDetails.setProductSuggestions(productSuggestionsApiService.getItem(productId));

        return productDetails;
    }

}
