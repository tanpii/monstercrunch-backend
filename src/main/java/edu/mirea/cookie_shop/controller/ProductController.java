package edu.mirea.cookie_shop.controller;

import edu.mirea.cookie_shop.dto.ProductDto;
import edu.mirea.cookie_shop.service.PictureService;
import edu.mirea.cookie_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Log4j2
public class ProductController {
    private final ProductService productService;
    private final PictureService pictureService;

    @GetMapping
    @SneakyThrows
    @CrossOrigin
    public List<ProductDto> getProducts() {
        return productService.getProducts().stream().map(entity ->
                new ProductDto(
                        entity.getProductId(),
                        entity.getProductName(),
                        entity.getDescription(),
                        entity.getPrice(),
                        entity.getAmount(),
                        URI.create(pictureService.getLinkOnPicture(entity.getProductName())))
        ).toList();
    }
}
