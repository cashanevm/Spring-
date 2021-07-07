package com.example.letscode.Controller;

import com.example.letscode.Domain.Dto.CaptchaResponseDto;
import com.example.letscode.Domain.User;
import com.example.letscode.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;


@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;

    @GetMapping("/registration")
    public String reg(){


        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponce,@RequestParam(name="password2") String password2, @Valid User user, BindingResult bindingResult, Model model){
        String url = String.format(CAPTCHA_URL, secret, captchaResponce);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }



        if (user.getPassword() != null && !user.getPassword().equals(password2)) {
            model.addAttribute("passwordCompError", "Passwords are different!");
        }

        if (bindingResult.hasErrors() || !response.isSuccess()||(user.getPassword() != null && !user.getPassword().equals(password2))) {
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
