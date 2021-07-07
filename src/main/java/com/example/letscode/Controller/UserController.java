package com.example.letscode.Controller;

import com.example.letscode.Domain.Role;
import com.example.letscode.Domain.User;
import com.example.letscode.Repository.UserRepository;
import com.example.letscode.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(Model model){
    model.addAttribute("users",userRepository.findAll());
        return "user-list";
    }
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user",user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @GetMapping("/delete/{user}")
    public String userDelete(@PathVariable User user){
        userService.deleteUser(user);
        return "redirect:/user";
    }

    @PostMapping
    public String userSave(
            @RequestParam("userId") User user,
            @RequestParam Map<String,String> form,
            @RequestParam String username
                           ){
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());



        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);

        return "redirect:/user";
    }
}
