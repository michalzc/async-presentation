package michalz.fancyshop.dto;

import lombok.*;

/**
 * Created by michal on 09.05.15.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    private Long productId;
    private String productName;
    private String productDescription;
}
