package michalz.fancyshop.dto;

import lombok.*;

import java.util.List;

/**
 * Created by michal on 09.05.15.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviews {
    private Long productId;
    private List<ProductReview> productReviews;
}
