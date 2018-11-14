package com.ssm.service.impl;

import com.ssm.dao.IUserDao;
import com.ssm.model.User;
import com.ssm.service.IUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User selectUser(long userId) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("id","1");
        Map<String,Object> result = namedParameterJdbcTemplate.queryForMap("SELECT * FROM user WHERE id=:id", param);
//        return this.userDao.selectUser(userId);
        User user = new User();
        user.setId(1);
        user.setUsername(result.get("username").toString());
        return user;
    }

    @Override
    public int insert(User user) {
        return 0;
    }

}