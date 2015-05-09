package michalz.fancyshop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by michal on 09.05.15.
 */
@Getter
@Setter
public class ProductDetails {
    private ProductInfo productInfo;
    private ProductSuggestions productSuggestions;
    private ProductReviews productReviews;
}
