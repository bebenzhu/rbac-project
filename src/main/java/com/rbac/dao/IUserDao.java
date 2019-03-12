package com.rbac.dao;
import com.rbac.app.login.entity.User;
public interface IUserDao {
    User selectUser(long id);
}