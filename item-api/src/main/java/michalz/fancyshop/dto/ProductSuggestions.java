package michalz.fancyshop.dto;

import lombok.*;

import java.util.List;

/**
 * Created by michal on 09.05.15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSuggestions {
    private Long productId;
    private List<ProductInfo> suggestedProducts;
}
