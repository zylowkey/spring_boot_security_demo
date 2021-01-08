package com.goldcard.springboot_security_demo.service.impl;

import com.goldcard.springboot_security_demo.pojo.DatabaseRole;
import com.goldcard.springboot_security_demo.pojo.DatabaseUser;
import com.goldcard.springboot_security_demo.repository.RoleRepository;
import com.goldcard.springboot_security_demo.repository.UserRepository;
import com.goldcard.springboot_security_demo.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Cacheable(value = "redisCache", key = "'redis_user_'+#userName")
    @Transactional
    public DatabaseUser getUserByName(String userName) {
        return userRepository.getUser(userName);
    }

    @Override
    @Cacheable(value = "redisCache", key = "'redis_user_role_'+#userName")
    @Transactional
    public List<DatabaseRole> findRolesByUserName(String userName) {
        return roleRepository.findRolesByUserName(userName);
    }
}
