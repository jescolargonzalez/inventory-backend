package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.company.inventory.dao.I_CategoryDao;
import com.company.inventory.dao.I_ProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

@Service
public class ProductService_IMPL implements I_ProductService{
//constructor ((@autowired)) inyeccion dependencias
    private I_CategoryDao categoryDao;      
    private I_ProductDao productDao;  

    public ProductService_IMPL(I_CategoryDao categoryDao ,I_ProductDao productDao){
        super();
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @Override
    public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
        
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

//Validacion de categorias para el producto
        try {
          //search category to set in the product object  
            Optional<Category> category =  categoryDao.findById(categoryId);
            if(category.isPresent()){
                product.setCategory(category.get());
            }else{
                response.setMetadata("FAIL", "-1", "Categoria no encontrada asociada al producto");
                return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
            }
//------------------------------------------
          //save the product
            Product productSaved = productDao.save(product);
            if(productSaved != null){
                list.add(productSaved);
                response.getProductResponse().setProducts(list);
                response.setMetadata("OK", "00", "producto guardado");
            }else{
                response.setMetadata("FAIL", "-1", "producto no guardado");
                return new ResponseEntity<ProductResponseRest>(response,HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("FAIL", "-1", "Error al guardar el producto");
            return new ResponseEntity<ProductResponseRest>(response,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);

    }    
}
