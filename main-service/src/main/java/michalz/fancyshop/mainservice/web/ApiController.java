package michalz.fancyshop.mainservice.web;

import michalz.fancyshop.dto.ProductDetails;
import michalz.fancyshop.mainservice.services.ProductDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * Created by michal on 09.05.15.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ProductDetailsService productDetailsService;

    @RequestMapping(value = "/productdetails/{productId}", method = RequestMethod.GET)
    public ProductDetails getProductDetails(@PathVariable("productId")Long productId) throws ExecutionException, InterruptedException {
        return productDetailsService.getProductDetails(productId);
    }
}
