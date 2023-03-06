package com.project.customer.controller;

import com.project.library.dto.CustomerDto;
import com.project.library.model.*;
import com.project.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CityService cityService;

    @GetMapping("/check-out")
    public String checkOut(Principal CheckoutPrincipal, Model Cartmodel) {
        if (CheckoutPrincipal == null) {
            return "redirect:/login";
        } else {
            CustomerDto customer = customerService.getCustomer(CheckoutPrincipal.getName());
            if (customer.getAddress() == null || customer.getCity() == null || customer.getPhoneNumber() == null) {
                Cartmodel.addAttribute("information", "Information required!");
                List<Country> countries = countryService.findAll();
                List<City> cities = cityService.findAll();
                Cartmodel.addAttribute("customer", customer);
                Cartmodel.addAttribute("cities", cities);
                Cartmodel.addAttribute("countries", countries);
                Cartmodel.addAttribute("title", "Profile");
                Cartmodel.addAttribute("page", "Profile");
                return "customer-information";
            } else {
                ShoppingCart cart = customerService.findByUsername(CheckoutPrincipal.getName()).getCart();
                Cartmodel.addAttribute("customer", customer);
                Cartmodel.addAttribute("title", "Check-Out");
                Cartmodel.addAttribute("page", "Check-Out");
                Cartmodel.addAttribute("shoppingCart", cart);
                Cartmodel.addAttribute("grandTotal", cart.getTotalItems());
                return "checkout";
            }
        }
    }

    @GetMapping("/orders")
    public String getOrders(Model OrderModel, Principal OrderPrincipal) {
        if (OrderPrincipal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(OrderPrincipal.getName());
            List<Order> orders = customer.getOrders();
            OrderModel.addAttribute("orders", orders);
            OrderModel.addAttribute("title", "Order");
            OrderModel.addAttribute("page", "Order");
            return "order";
        }
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long OrderId, RedirectAttributes OrderAttributes){
        orderService.cancelOrder(OrderId);
        OrderAttributes.addFlashAttribute("success", "Cancel order successfully!");
        return "redirect:/orders";
    }


    @RequestMapping(value = "/add-order", method = {RequestMethod.POST})
    public String createOrder(Principal principal,
                              Model model,
                              HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();
            Order order = orderService.save(cart);
            session.removeAttribute("totalItems");
            model.addAttribute("order", order);
            model.addAttribute("title", "Order Detail");
            model.addAttribute("page", "Order Detail");
            model.addAttribute("success", "Add order successfully");
            return "order-detail";
        }
    }

}
