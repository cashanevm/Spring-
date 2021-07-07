package com.example.letscode.Controller;

import com.example.letscode.Domain.Role;
import com.example.letscode.Domain.User;
import com.example.letscode.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;


@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String reg(){


        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(User user, Model model){
        User userFromBd = userRepository.findByUsername(user.getUsername());
        if(userFromBd != null){
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }
}
