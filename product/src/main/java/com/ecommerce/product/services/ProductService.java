package com.ecommerce.product.services;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse updateProduct(String productId, ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(String productId);

    void deleteProduct(String productId);

    List<ProductResponse> searchProduct(String keyword);
}
