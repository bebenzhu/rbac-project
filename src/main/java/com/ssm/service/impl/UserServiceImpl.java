package com.ssm.service.impl;

import com.ssm.dao.IUserDao;
import com.ssm.model.User;
import com.ssm.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public User selectUser(long userId) {
        return this.userDao.selectUser(userId);
    }

    @Override
    public int insert(User user) {
        return 0;
    }

}