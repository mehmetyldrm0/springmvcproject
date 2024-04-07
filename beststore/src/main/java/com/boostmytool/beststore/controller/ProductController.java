package com.boostmytool.beststore.controller;

import com.boostmytool.beststore.dto.ProductDto;
import com.boostmytool.beststore.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping({ "", "/" })
    public String showProductList(Model model) {
        return productService.showProductList(model);
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        return productService.showCreatePage(model);
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult bindingResult) {
        return productService.createProduct(productDto,bindingResult);
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        return productService.showEditPage(model,id);
    }

    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDto productDto,
                                BindingResult result) {
        return productService.updateProduct(model,id,productDto,result);
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {
        return productService.deleteProduct(id);
    }
}
