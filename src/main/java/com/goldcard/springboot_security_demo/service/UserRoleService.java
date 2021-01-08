package com.goldcard.springboot_security_demo.service;

import com.goldcard.springboot_security_demo.pojo.DatabaseRole;
import com.goldcard.springboot_security_demo.pojo.DatabaseUser;

import java.util.List;

public interface UserRoleService {
    DatabaseUser getUserByName(String userName);

    List<DatabaseRole> findRolesByUserName(String userName);
}
