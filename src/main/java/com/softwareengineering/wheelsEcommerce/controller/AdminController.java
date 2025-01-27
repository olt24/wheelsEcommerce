package com.softwareengineering.wheelsEcommerce.controller;

import com.softwareengineering.wheelsEcommerce.model.Order;
import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.model.ProductType;
import com.softwareengineering.wheelsEcommerce.service.OrderService;
import com.softwareengineering.wheelsEcommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String manageProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("action", "add");
        return "admin/addProduct";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product) {
        if (product.getProductType() == ProductType.RIM) {
            product.setSize(null);
            product.setType(null);
        } else if (product.getProductType() == ProductType.TIRE) {
            product.setDiameter(null);
            product.setMaterial(null);
        }
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("action", "edit");
        return "admin/addProduct";
    }

    @PostMapping("/products/edit")
    public String editProduct(@ModelAttribute Product product) {
        if (product.getProductType() == ProductType.RIM) {
            product.setSize(null);
            product.setType(null);
        } else if (product.getProductType() == ProductType.TIRE) {
            product.setDiameter(null);
            product.setMaterial(null);
        }
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}