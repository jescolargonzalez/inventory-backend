package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.I_CategoryDao;
import com.company.inventory.dao.I_ProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.Util;

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
//--------------------------------------------------

//GUARDAR
    @Override
    @Transactional
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
            return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
    }


//Buscar por ID
    @Override
    @Transactional (readOnly = true)
    public ResponseEntity<ProductResponseRest> searchById(Long id) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();
        try {
          //search product by id  
            Optional<Product> product = productDao.findById(id);
            if(product.isPresent()){
                
                byte[] imageDescompressed = Util.decompressZLib(product.get().getPicture());
                product.get().setPicture(imageDescompressed);
                list.add(product.get());
                response.getProductResponse().setProducts(list);
                response.setMetadata("OK", "00", "producto encontrado");                 
            }else{
                response.setMetadata("FAIL", "-1", "Producto no encontrado");
                return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("FAIL", "-1", "Error al guardar el producto");
            return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
    }


    //Filtro de busqueda
    @Override
    @Transactional (readOnly = true)
    public ResponseEntity<ProductResponseRest> searchByName(String name) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();
        List<Product> listAux = new ArrayList<>();
        try {
        //search product by name
            listAux = productDao.findByNameContainingIgnoreCase(name);            
        
            if(listAux.size()>0){
                listAux.stream().forEach((p) -> {
                    byte[] imageDescompressed = Util.decompressZLib(p.getPicture());
                    p.setPicture(imageDescompressed);
                    list.add(p);
                });
                response.getProductResponse().setProducts(list);
                response.setMetadata("OK", "00", "productos encontrados");                 
            }else{
                response.setMetadata("FAIL", "-1", "Productos no encontrados");
                return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("FAIL", "-1", "Error al buscar el producto por nombre");
            return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> deleteById(Long id) {
        ProductResponseRest response = new ProductResponseRest();        
        try {
          //delete product by id  
            productDao.deleteById(id);
            response.setMetadata("FAIL", "-1", "Producto ELIMINADO");    
        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("FAIL", "-1", "Error al eliminar el producto");
            return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
    }    
}
