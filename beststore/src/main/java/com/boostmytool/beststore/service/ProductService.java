package com.boostmytool.beststore.service;

import com.boostmytool.beststore.dto.ProductDto;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface ProductService {

    String showProductList(Model model);

    String showCreatePage(Model model);

    String createProduct(ProductDto productDto, BindingResult bindingResult);

    String showEditPage(Model model, int id);

    String updateProduct(Model model, int id, ProductDto productDto, BindingResult result);

    String deleteProduct(int id);
}
