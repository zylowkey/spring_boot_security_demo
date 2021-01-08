package com.goldcard.springboot_security_demo.repository;

import com.goldcard.springboot_security_demo.pojo.DatabaseUser;

public interface UserRepository {
    DatabaseUser getUser(String userNames);
}
