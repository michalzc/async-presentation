package michalz.fancyshop.externalservices.api;

import lombok.extern.slf4j.Slf4j;
import michalz.fancyshop.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by michal on 09.05.15.
 */
@RestController
@RequestMapping("/api/")
@Slf4j
public class ExternalApiController {

    @Value("${fancyshop.productinfo.delay}")
    private Long productInfoDelay;

    @Value("${fancyshop.productreviews.delay}")
    private Long productReviewsDelay;

    @Value("${fancyshop.productsuggestions.delay}")
    private Long productSuggestionsDelay;


    @RequestMapping(value = "/productinfo/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ProductInfo getProductInfo(@PathVariable("productId") Long productId) {
        delay(productInfoDelay);
        log.info("Product info for {}", productId);
        return ProductInfo.builder().productId(productId).productName("Example Product")
                .productDescription("Example product descriptions").build();
    }

    @RequestMapping(value = "/productreviews/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ProductReviews getProductReviews(@PathVariable("productId") Long productId) {

        delay(productReviewsDelay);
        log.info("Product reviews for {}", productId);
        return ProductReviews.builder().productId(productId).productReviews(Arrays.asList(
                ProductReview.builder().reviewerName("Porfirij Jebanow").reviewerEmail("porfirij@yandex.ru")
                        .addDate(new Date()).productRating(ProductRating.GOOD).review("Haroszije!").build(),
                ProductReview.builder().reviewerName("Stan Kotushchyk").reviewerEmail("stan@yahoo.com")
                        .addDate(new Date()).productRating(ProductRating.GREAT).review("It's great shfagier!").build()
        )).build();
    }

    @RequestMapping(value = "/productsuggestions/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ProductSuggestions getProductSuggestions(@PathVariable("productId") Long productId) {

        delay(productSuggestionsDelay);
        log.info("Product suggestions for {}", productId);
        return ProductSuggestions.builder().productId(productId).suggestedProducts(Arrays.asList(
                ProductInfo.builder().productId(productId + 1).productName("Additional Product")
                        .productDescription("Additional product description").build(),
                ProductInfo.builder().productId(productId + 2).productName("Second additional product")
                        .productDescription("Second description").build()
        )).build();
    }

    private void delay(Long productInfoDelay) {
        try {
            Thread.sleep(productInfoDelay);
        } catch (InterruptedException e) {
            log.warn("Delay interrupted", e);
        }
    }

}
