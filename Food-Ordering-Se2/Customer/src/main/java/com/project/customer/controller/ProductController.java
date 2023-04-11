package com.project.customer.controller;

import com.project.library.dto.CategoryDto;
import com.project.library.dto.ProductDto;
import com.project.library.model.Category;
import com.project.library.service.CategoryService;
import com.project.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/menu")
    public String menu(Model productModel){
        productModel.addAttribute("page", "Products");
        productModel.addAttribute("title", "Menu");
        List<Category> categoriesList = categoryService.findAllByActivatedTrue();
        List<ProductDto> productsList = productService.products();
        productModel.addAttribute("products", productsList);
        productModel.addAttribute("categories", categoriesList);
        return "index";
    }


    @GetMapping("/product-detail/{id}")
    public String details(@PathVariable("id")Long id, Model productModel){
        ProductDto productDto = productService.getById(id);
        List<ProductDto> productDtoList = productService.findAllByCategory(productDto.getCategory().getName());
        productModel.addAttribute("products", productDtoList);
        productModel.addAttribute("title", "Product Detail");
        productModel.addAttribute("page", "Product Detail");
        productModel.addAttribute("productDetail", productDto);
        return "product-detail";
    }

    @GetMapping("/shop-detail")
    public String shopDetail(Model productModel){
        List<CategoryDto> categoriesDtos = categoryService.getCategoriesAndSize();
        productModel.addAttribute("categories", categoriesDtos);
        List<ProductDto> productsList = productService.randomProduct();
        List<ProductDto> listView = productService.listViewProducts();
        productModel.addAttribute("productViews", listView);
        productModel.addAttribute("title", "Shop Detail");
        productModel.addAttribute("page", "Shop Detail");
        productModel.addAttribute("products", productsList);
        return "shop-detail";
    }


    @GetMapping("/high-price")
    public String filterHighPrice(Model model){
        List<CategoryDto> categoriesDtos = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categoriesDtos);
        List<ProductDto> productsList = productService.filterHighProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("productViews", listView);
        model.addAttribute("products", productsList);
        return "shop-detail";
    }


    @GetMapping("/lower-price")
    public String filterLowerPrice(Model model){
        List<CategoryDto> categoriesDtos = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categoriesDtos);
        List<ProductDto> productsList = productService.filterLowerProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", productsList);
        return "shop-detail";
    }

    @GetMapping("/find-products/{id}")
    public String productsInCategory(@PathVariable("id")Long id, Model model){
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.findByCategoryId(id);
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", productDtos.get(0).getCategory().getName());
        model.addAttribute("page", "Product-Category");
        model.addAttribute("products", productDtos);
        return "products";
    }


    @GetMapping("/search-product")
    public String searchProduct(@RequestParam("keyword")String keyword, Model model){
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.searchProducts(keyword);
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", "Search Products");
        model.addAttribute("page", "Result Search");
        model.addAttribute("products", productDtos);
        return "products";
    }
}
