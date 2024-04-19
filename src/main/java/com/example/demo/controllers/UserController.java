package com.example.demo.controllers;

import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,Model model){
        User existingUser = userService.findByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            return "redirect:/register?error";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }


    @PostMapping("/users/delete")
    public String usersDelete(@Valid @ModelAttribute("email") String email, Model model){

        userService.deleteUser(email);
        return "redirect:/users?delete";

    }

    @PostMapping("/users/edit")
    public String usersEdit(@Valid @ModelAttribute("email") String email, Model model){
        //userService.editUser(user);
        User user=userService.findByEmail(email);
        model.addAttribute("user", user);
        return "edit";

    }

    @PostMapping("/users/save")
    public String usersEditSave(@Valid @ModelAttribute("user") UserDto userDto,Model model){
        userService.editUser(userDto);
        return "redirect:/users";
    }
}
