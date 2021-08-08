package com.example.Spring.with.json.format.service;

import com.example.Spring.with.json.format.model.entity.User;

import java.io.IOException;

public interface UserService {
    void seedUsersDB() throws IOException;
    User findUser();

    String getAllUsersWithAtLeastOneItemSoldWithBuyer();


}
