package com.boostmytool.beststore.service;

import com.boostmytool.beststore.dto.ProductDto;
import com.boostmytool.beststore.entity.Product;
import com.boostmytool.beststore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public String showProductList(Model model) {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);

        return "products/index";
    }

    @Override
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/CreateProduct";
    }

    @Override
    public String createProduct(ProductDto productDto, BindingResult bindingResult) {
        if (productDto.getImageFile().isEmpty()) {
            bindingResult.addError(new FieldError("productDto", "imageFile", "The image file is required"));
        }
        if (bindingResult.hasErrors()) {
            return "products/CreateProduct";
        }
        MultipartFile image = productDto.getImageFile();
        Date createDate = new Date();
        String storageFileName = createDate.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir, storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // insert...
        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createDate);
        product.setImageFileName(storageFileName);

        productRepository.save(product);

        return "redirect:/products"; // ürün listesi sınıfına yönlendirme!
    }

    @Override
    public String showEditPage(Model model, int id) {

        try {

            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();

            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());
            model.addAttribute("productDto", productDto);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/products";
        }

        return "products/EditProduct";
    }

    @Override
    public String updateProduct(Model model, int id, ProductDto productDto, BindingResult result) {
        try {

            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);

            if (result.hasErrors()) {
                return "products/EditProduct";
            }

            if (!productDto.getImageFile().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.delete(oldImagePath);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }

                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String strogeFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + strogeFileName), StandardCopyOption.REPLACE_EXISTING);

                }

                product.setImageFileName(strogeFileName);
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            productRepository.save(product);


        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @Override
    public String deleteProduct(int id) {
        try {

            Product product = productRepository.findById(id).get();

            Path imagePath = Paths.get("public/images/" + product.getImageFileName());
            Files.delete(imagePath);

            productRepository.delete(product);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());

        }

        return "redirect:/products";
    }
}
