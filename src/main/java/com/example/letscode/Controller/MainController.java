package com.example.letscode.Controller;

import com.example.letscode.Domain.Message;
import com.example.letscode.Domain.User;
import com.example.letscode.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Binding;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

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
    public String postMessage(@Valid Message message, BindingResult bindingResult, @AuthenticationPrincipal User user,
                              @RequestParam("file") MultipartFile file, Model model) throws IOException {
        message.setAuthor(user);

        if(bindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrorsMap(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message",message);
            return "add";
        }else{

        if(file != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String finalFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath+ "/"+finalFileName));

            message.setFilename(finalFileName);
        }
        model.addAttribute("message",null);
        messageRepository.save(message);
        }
        return "redirect:/main";
    }



    @PostMapping("/delete")
    public String deleteMessage(@RequestParam(name ="id")  String id){
        messageRepository.deleteById(Long.parseLong(id));
        return "redirect:/main";


    }







}
