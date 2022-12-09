package com.company.inventory.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.I_ProductService;
import com.company.inventory.util.Util;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {

    private I_ProductService productService;

    public ProductRestController(I_ProductService productService) {
        super();
        this.productService = productService;
    }

    // Guardar producto
    @PostMapping("/products")
    public ResponseEntity<ProductResponseRest> save(@RequestParam("picture") MultipartFile picture,
            @RequestParam("name") String name,
            @RequestParam("price") Double precio, @RequestParam("cantidad") int cantidad,
            @RequestParam("categoryId") Long categoryId) throws IOException {

        Product product = new Product();
        product.setName(name);
        product.setPrecio(precio);
        product.setCantidad(cantidad);
        product.setPicture(Util.compressZLib(picture.getBytes()));

        ResponseEntity<ProductResponseRest> response = productService.save(product, categoryId);

        return response;
    }

    // Obtiene producto por el id
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> searchById(@PathVariable Long id) {
        ResponseEntity<ProductResponseRest> response = productService.searchById(id);
        return response;
    }

    // Obtiene producto por el nombre
    @GetMapping("/products/filter/{name}")
    public ResponseEntity<ProductResponseRest> searchByName(@PathVariable String name) {
        ResponseEntity<ProductResponseRest> response = productService.searchByName(name);
        return response;
    }

    // ELIMINA producto por el id
    @DeleteMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> deleteById(@PathVariable Long id) {
        ResponseEntity<ProductResponseRest> response = productService.deleteById(id);
        return response;
    }

}
