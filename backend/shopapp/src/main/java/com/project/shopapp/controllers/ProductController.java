package com.project.shopapp.controllers;
import com.project.shopapp.dtos.ProductDTO;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @GetMapping("")
    public ResponseEntity<String> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok(String.format("getAllProducts, page = %d, limit = %d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable("id") String productId) {

        return ResponseEntity.ok("Product with ID: " + productId);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
//            @RequestPart("file") MultipartFile file,
            BindingResult result
    ) {
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            for(MultipartFile file : files){
                if(file.getSize() == 0){
                    continue;
                }
                // Kiem tra kich thuoc file va dinh dang
                if(file.getSize() > 10 * 1024 * 1024){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }
                // luu file va cap nhat thumbnail trong DTO
                String filename = storeFile(file);
            }

            return ResponseEntity.ok("Them thanh cong " + productDTO.getName());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException{
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // Them UUID vao truoc ten file de dam bao ten file la duy nhat
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Duong dan den thu muc ma ban muon luu file
       java.nio.file.Path uploadDir = Paths.get("uploads");
       // kiem tra va tao thu muc neu no khong ton tai
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        // Duong dan day du den file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chep file vao thu muc dich
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id){
        return ResponseEntity.ok("cap nhat thanh cong id ="+ id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", id));
    }
}
