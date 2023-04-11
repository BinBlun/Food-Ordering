package com.project.customer.controller;

import com.project.library.dto.CustomerDto;
import com.project.library.model.Customer;
import com.project.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Login Page");
        model.addAttribute("page", "Home");
        return "login";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("page", "Register");
        model.addAttribute("customerDto", new CustomerDto());
        return "register";
    }


    @PostMapping("/do-register")
    public String registerCustomer(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                   BindingResult bindingResult,
                                   Model customerModel) {
        try {
            if (bindingResult.hasErrors()) {
                customerModel.addAttribute("customerDto", customerDto);
                return "register";
            }
            String username = customerDto.getUsername();
            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                customerModel.addAttribute("customerDto", customerDto);
                customerModel.addAttribute("error", "Email has been register!");
                return "register";
            }
            if (customerDto.getPassword().equals(customerDto.getConfirmPassword())) {
                customerDto.setPassword(bCryptPasswordEncoder.encode(customerDto.getPassword()));
                customerService.save(customerDto);
                customerModel.addAttribute("success", "Register successfully!");
            } else {
                customerModel.addAttribute("error", "Password is incorrect");
                customerModel.addAttribute("customerDto", customerDto);
                return "register";
            }
        } catch (Exception e) {
            e.printStackTrace();
            customerModel.addAttribute("error", "Server is error, try again later!");
        }
        return "register";
    }


}
