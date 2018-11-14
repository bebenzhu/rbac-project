package com.ssm.service;

import com.ssm.model.User;


public interface IUserService {

    User selectUser(long userId);

    int insert(User user);

}