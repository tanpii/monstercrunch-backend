package edu.mirea.cookie_shop.controller;

import edu.mirea.cookie_shop.dto.ProductDto;
import edu.mirea.cookie_shop.dto.requests.ProductRequest;
import edu.mirea.cookie_shop.dto.response.IsFavoriteResponse;
import edu.mirea.cookie_shop.service.FavoriteProductService;
import edu.mirea.cookie_shop.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fav")
public class FavoriteProductController {
    private final FavoriteProductService favoriteProductService;
    private final JwtService jwtService;

    private static final String TOKEN = "Token";

    @GetMapping
    @SneakyThrows
    @CrossOrigin
    public List<ProductDto> getFavorite(@RequestHeader(TOKEN) String token) {
        String email = jwtService.extractUserName(token);
        return favoriteProductService.getFavoriteProducts(email);
    }

    @GetMapping("/{id}")
    @CrossOrigin
    public IsFavoriteResponse isFavorite(@RequestHeader(TOKEN) String token, @PathVariable Long id) {
        String email = jwtService.extractUserName(token);
        return new IsFavoriteResponse(favoriteProductService.isProductFavorite(email, id));
    }

    @PostMapping
    @CrossOrigin
    public void addToFavorite(@RequestHeader(TOKEN) String token, @RequestBody ProductRequest request) {
        String email = jwtService.extractUserName(token);
        favoriteProductService.addFavouriteProduct(email, request.productId());
    }

    @DeleteMapping
    @CrossOrigin
    public void removeFromFavorite(@RequestHeader(TOKEN) String token, @RequestBody ProductRequest request) {
        String email = jwtService.extractUserName(token);
        favoriteProductService.removeFavoriteProduct(email, request.productId());
    }
}
