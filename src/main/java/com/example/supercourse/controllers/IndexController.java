package com.example.supercourse.controllers;

import com.example.supercourse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{name}")
    public String index(Model model, @PathVariable String name) {
        model.addAttribute("number", name);
        return "index";
    }
}
