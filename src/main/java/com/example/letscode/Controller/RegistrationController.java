package com.example.letscode.Controller;

import com.example.letscode.Domain.User;
import com.example.letscode.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;


@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String reg(){


        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model){

        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordCompError", "Passwords are different!");
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrorsMap(bindingResult);

            model.mergeAttributes(errors);
            model.addAttribute("user",user);
            return "registration";
        }


        if(!userService.addUser(user)){
            model.addAttribute("userError", "User exists!");
            return "registration";
        }
        model.addAttribute("message", "Сonfirm your email: "+ user.getEmail()+" please!");
    return "login";
    }
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }


        return "login";
    }
}
