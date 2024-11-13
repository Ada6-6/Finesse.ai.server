package com.powerpuff.billServer.service;

import com.powerpuff.billServer.model.User;

import java.util.List;

public interface UserService {
    //save user (add and modify)
    public String saveUser(User user);

    //list non delete user(using_type != 3)
    public List<User> getAllUser();

    public String deleteUser(Integer id);
}
