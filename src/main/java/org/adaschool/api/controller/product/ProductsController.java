package org.adaschool.api.controller.product;

import org.adaschool.api.exception.ProductNotFoundException;
import org.adaschool.api.repository.product.Product;
import org.adaschool.api.repository.product.ProductDto;
import org.adaschool.api.service.product.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/products/")
public class ProductsController {

    private final ProductsService productsService;

    @Autowired

    public ProductsController(@Autowired ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        URI createdProductUri = URI.create("");
        if (!productsService.findById(product.getId()).isEmpty()) {
            throw new ProductNotFoundException(product.getId());
        }
        productsService.save(product);
        return ResponseEntity.created(createdProductUri).body(null);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productsService.all());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") String id) {
        Optional<Product> p = productsService.findById(id);
        if (p.isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        return ResponseEntity.ok(p.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable("id") String id) {
        Product pr = new Product();
        Optional<Product> prod = productsService.findById(id);
        if (prod.isPresent()) {
            productsService.update(pr, id);
            productsService.save(prod.get());
            return ResponseEntity.ok(pr);
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        if (productsService.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        } else {
            productsService.deleteById(id);
        }
        return ResponseEntity.ok().build();
    }
}