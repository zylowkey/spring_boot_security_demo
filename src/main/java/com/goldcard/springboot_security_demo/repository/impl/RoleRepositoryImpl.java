package com.goldcard.springboot_security_demo.repository.impl;

import com.goldcard.springboot_security_demo.mapper.RoleMapper;
import com.goldcard.springboot_security_demo.pojo.DatabaseRole;
import com.goldcard.springboot_security_demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class RoleRepositoryImpl implements RoleRepository {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<DatabaseRole> findRolesByUserName(String userName) {
        return roleMapper.findRolesByUserName(userName);
    }
}
