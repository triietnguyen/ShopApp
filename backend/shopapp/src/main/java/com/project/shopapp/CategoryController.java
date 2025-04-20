package com.project.shopapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
    //Hien thi tat ca category
    @GetMapping("")
    public ResponseEntity<String> getAllCategories(){
        return ResponseEntity.ok("Hello Triet");
    }

    @PostMapping("")
    public ResponseEntity<String> insertCategory(){
        return ResponseEntity.ok("Them thanh cong");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id){
        return ResponseEntity.ok("cap nhat thanh cong id ="+ id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        return ResponseEntity.ok("xoa thanh cong voi id = "+ id);
    }
}
