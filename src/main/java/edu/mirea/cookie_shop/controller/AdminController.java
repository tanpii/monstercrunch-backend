package edu.mirea.cookie_shop.controller;

import edu.mirea.cookie_shop.dto.requests.AddNewProductRequest;
import edu.mirea.cookie_shop.dto.requests.ProductRequest;
import edu.mirea.cookie_shop.service.PictureService;
import edu.mirea.cookie_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Log4j2
public class AdminController {
    private final ProductService productService;
    private final PictureService pictureService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @SneakyThrows
    @CrossOrigin
    public void addProduct(@ModelAttribute AddNewProductRequest request) {
        pictureService.putProductPicture(request.productName(), request.image().getInputStream());
        productService.addProduct(request);
    }

    @DeleteMapping
    @CrossOrigin
    public void deleteProduct(@RequestBody ProductRequest request) {
        productService.deleteProduct(request.productId());
    }
}
