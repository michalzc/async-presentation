package michalz.fancyshop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by michal on 09.05.15.
 */
@Getter
@Setter
public class ProductInfo {
    private Long productId;
    private String productName;
    private String productDescription;
}
