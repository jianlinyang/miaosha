package com.shu.miaosha.service;

import com.shu.miaosha.dao.UserDao;
import com.shu.miaosha.domain.User;
import com.shu.miaosha.result.CodeMsg;
import com.shu.miaosha.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @date 2019/6/27 23:48
 */
@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getById(Integer id) {
        return userDao.getById(id);
    }


}
