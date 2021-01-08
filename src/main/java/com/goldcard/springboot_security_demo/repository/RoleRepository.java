package com.goldcard.springboot_security_demo.repository;

import com.goldcard.springboot_security_demo.pojo.DatabaseRole;

import java.util.List;

public interface RoleRepository {
    List<DatabaseRole> findRolesByUserName(String userName);
}
