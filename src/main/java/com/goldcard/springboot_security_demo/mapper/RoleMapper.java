package com.goldcard.springboot_security_demo.mapper;

import com.goldcard.springboot_security_demo.pojo.DatabaseRole;

import java.util.List;

public interface RoleMapper {
     List<DatabaseRole> findRolesByUserName(String userName);
}
