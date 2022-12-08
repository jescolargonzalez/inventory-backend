package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.company.inventory.dao.I_CategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService_IMPL implements I_CategoryService{

@Autowired
private I_CategoryDao categoryDao;

@Override
@Transactional(readOnly = true)
public ResponseEntity<CategoryResponseRest> search() {
    CategoryResponseRest response = new CategoryResponseRest();
    try {
        List<Category> category = (List<Category>) categoryDao.findAll();
        response.getCategoryResponse().setCategory(category);
        response.setMetadata("Respuesta OK", "00", "Respuesta existosa");
    } catch (Exception e) {
        response.setMetadata("Respuesta fallida", "-1", "Error al consultar");
        e.getStackTrace();
        return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.OK);
}

@Override
@Transactional(readOnly = true)
public ResponseEntity<CategoryResponseRest> searchById(Long id) {
    CategoryResponseRest response = new CategoryResponseRest();
    List<Category> list = new ArrayList<>();
    try {
        Optional<Category> category = categoryDao.findById(id);
        if(category.isPresent()){
            list.add(category.get());
            response.getCategoryResponse().setCategory(list);
            response.setMetadata("Respuesta OK", "00", "Categoria encontrada.");
        } else {
            response.setMetadata("Respuesta fallida", "-1", "Categoria NO encontrada.");
            return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        response.setMetadata("Respuesta fallida", "-1", "Error al consultar por ID");
        e.getStackTrace();
        return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.OK);
            
}

@Override
@Transactional
public ResponseEntity<CategoryResponseRest> save(Category category) {
    CategoryResponseRest response = new CategoryResponseRest();
    List<Category> list = new ArrayList<>();
    try {
        Category categorySaved = categoryDao.save(category);
        if(categorySaved != null){
            list.add(categorySaved);
            response.getCategoryResponse().setCategory(list);
            response.setMetadata("Respuesta ok", "00", "Categoria guardada");
        }else{
            response.setMetadata("Respuesta fallida", "-1", "Categoria no guardada");
            return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.BAD_REQUEST);
        }
    } catch (Exception e) {
        response.setMetadata("Respuesta fallida", "-1", "Error al guardar categoria");
        e.getStackTrace();
        return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.OK);
}

@Override
@Transactional
public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {
    CategoryResponseRest response = new CategoryResponseRest();
    List<Category> list = new ArrayList<>();
    try {
        Optional<Category> categorySearch = categoryDao.findById(id);
        if(categorySearch.isPresent()){
            // se procedera a actualizar el registro
            categorySearch.get().setName(category.getName());
            categorySearch.get().setDescription(category.getDescription());

            Category categoryToUpdate = categoryDao.save(categorySearch.get());
            if(categoryToUpdate != null){
                list.add(categoryToUpdate);
                response.getCategoryResponse().setCategory(list);
                response.setMetadata("Respuesta ok", "00", "Categoria Actualizada");
            }else{
                response.setMetadata("Respuesta fallida", "-1", "Categoria NO actualizada");
                return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.BAD_REQUEST);
            }

        }else{
            response.setMetadata("Respuesta fallida", "-1", "Categoria NO encontrada");
            return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.NOT_FOUND);
        }
    
    } catch (Exception e) {
        response.setMetadata("Respuesta fallida", "-1", "Error al actualizar categoria");
        e.getStackTrace();
        return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.OK);
}

@Override
@Transactional
public ResponseEntity<CategoryResponseRest> deleteById (Long id) {
    CategoryResponseRest response = new CategoryResponseRest();
    try{
        categoryDao.deleteById(id);
        response.setMetadata("Respuesta OK", "00", "Registro Eliminado");           
    }catch(Exception e){
        response.setMetadata("Respuesta fallida", "-1", "Error al eliminar");
        e.getStackTrace();
        return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);        
    }
    return new ResponseEntity<CategoryResponseRest>(response,HttpStatus.OK);    
}     


}
