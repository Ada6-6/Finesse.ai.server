package com.powerpuff.billServer.controller;

import com.powerpuff.billServer.model.User;
import com.powerpuff.billServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public String add(@RequestBody User user){
        userService.saveUser(user);
        return user.getId() != null ? "User has been updated" : "New user has been added";
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        userService.deleteUser(id);
        return "User with ID " + id + " is logically delete (using_type set to 3)";

    }
}
