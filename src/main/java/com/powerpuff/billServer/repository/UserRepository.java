package com.powerpuff.billServer.repository;

import com.powerpuff.billServer.model.User;
import com.powerpuff.billServer.model.UsingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    // 查找 using_type 不等于 3 的用户
    List<User> findByUsingTypeNot(UsingType usingType);
}
