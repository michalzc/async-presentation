package michalz.fancyshop.mainservice;

import akka.actor.ActorSystem;
import michalz.fancyshop.dto.ProductInfo;
import michalz.fancyshop.dto.ProductReviews;
import michalz.fancyshop.dto.ProductSuggestions;
import michalz.fancyshop.mainservice.services.ApiService;
import michalz.fancyshop.mainservice.services.ProductDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * Created by michal on 09.05.15.
 */
@Configuration
@EnableAsync
public class ServiceConfiguration {

    @Value("${rest.external.infoUrl}")
    private String infoUrl;

    @Value("${rest.external.reviewsUrl}")
    private String reviewsUrl;

    @Value("${rest.external.suggestionsUrl}")
    private String suggestionsUrl;


    @Bean
    public ApiService<ProductInfo> productInfoApiService() {
        return new ApiService<>(ProductInfo.class, infoUrl);
    }

    @Bean
    public ApiService<ProductReviews> productReviewsApiService() {
        return new ApiService<>(ProductReviews.class, reviewsUrl);
    }

    @Bean
    public ApiService<ProductSuggestions> productSuggestionsApiService() {
        return new ApiService<>(ProductSuggestions.class, suggestionsUrl);
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate(new HttpComponentsAsyncClientHttpRequestFactory());
    }

    @Bean
    public ProductDetailsService productDetailsService() {
        return new ProductDetailsService();
    }

    @Bean
    public ActorSystem actorSystem() {
        return ActorSystem.create("FancyShopSystem");
    }


}
