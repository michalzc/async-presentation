package michalz.fancyshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by michal on 09.05.15.
 */
@Getter
@Setter
public class ProductDetails {
    private ProductInfo productInfo;
    private ProductSuggestions productSuggestions;
    private List<ProductReview> productReviews;
}
