package com.goldcard.springboot_security_demo.mapper;

import com.goldcard.springboot_security_demo.pojo.DatabaseUser;

public interface UserMapper {
    DatabaseUser getUser(String userName);
}
