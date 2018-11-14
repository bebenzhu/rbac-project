package com.ssm.dao;
import com.ssm.model.User;
public interface IUserDao {
    User selectUser(long id);
}