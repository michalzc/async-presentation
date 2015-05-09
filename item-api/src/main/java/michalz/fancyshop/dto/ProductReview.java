package michalz.fancyshop.dto;

import lombok.*;

import java.util.Date;

/**
 * Created by michal on 09.05.15.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {
    private String reviewerName;
    private String reviewerEmail;
    private ProductRating productRating;
    private Date addDate;
    private String review;
}
