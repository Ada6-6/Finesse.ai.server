package com.powerpuff.billServer.service;

import com.powerpuff.billServer.model.User;
import com.powerpuff.billServer.model.UsingType;
import com.powerpuff.billServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public String saveUser(User user) {
        userRepository.save(user);
        return "Saved successfully.";
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findByUsingTypeNot(UsingType.DELETED);
    }

    @Override
    public String deleteUser(Integer id){
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            user.setUsingType(UsingType.DELETED);
            userRepository.save(user);
            return "Deleted successfully.";
        }else {
            return "can not find this record.";
        }
    }
}
