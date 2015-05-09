package michalz.fancyshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by michal on 09.05.15.
 */
@Getter
@Setter
public class ProductSuggestions {
    private Long productId;
    private List<ProductInfo> suggestedProducts;
}
