package michalz.fancyshop.mainservice.web;

import lombok.extern.slf4j.Slf4j;
import michalz.fancyshop.dto.ProductDetails;
import michalz.fancyshop.mainservice.services.ProductDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutionException;

/**
 * Created by michal on 09.05.15.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    @Autowired
    private ProductDetailsService productDetailsService;

    @RequestMapping(value = "/productdetails/{productId}", method = RequestMethod.GET)
    public DeferredResult<ProductDetails> getProductDetails(@PathVariable("productId")Long productId) throws ExecutionException, InterruptedException {
        log.info("Handling productdetails request for {}", productId);
        DeferredResult<ProductDetails> result = new DeferredResult<>();
        productDetailsService.productDetails(productId, result);
        return result;
    }
}
