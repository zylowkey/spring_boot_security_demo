package com.goldcard.springboot_security_demo.repository.impl;

import com.goldcard.springboot_security_demo.mapper.UserMapper;
import com.goldcard.springboot_security_demo.pojo.DatabaseUser;
import com.goldcard.springboot_security_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private UserMapper userMapper;
    @Override
    public DatabaseUser getUser(String userName) {
        return userMapper.getUser(userName);
    }
}
