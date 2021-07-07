package com.example.letscode.Controller;

import com.example.letscode.Domain.Message;
import com.example.letscode.Domain.User;
import com.example.letscode.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {


    @Autowired
    public MessageRepository messageRepository;

    @GetMapping("/")
    public String greeting(){
        return "greeting";
    }


    @GetMapping("/main")
    public String showIndex(Model model,@AuthenticationPrincipal User user){

        Iterable<Message> messages = messageRepository.findByAuthor(user);
        model.addAttribute("messages", messages);
        return "index";
    }
    @GetMapping("/add")
    public String addMessage(Message message){
        return "add";
    }
    @PostMapping("/add")
    public String postMessage(Message message, @AuthenticationPrincipal User user){
        message.setAuthor(user);
        messageRepository.save(message);
        return "redirect:/main";
    }
    @PostMapping("/delete")
    public String deleteMessage(@RequestParam(name ="id")  String id){
        messageRepository.deleteById(Long.parseLong(id));
        return "redirect:/main";


    }







}
