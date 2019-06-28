package com.shu.miaosha.service;

import com.shu.miaosha.dao.MiaoshaUserDao;
import com.shu.miaosha.domain.MiaoshaUser;
import com.shu.miaosha.result.CodeMsg;
import com.shu.miaosha.utils.MD5Util;
import com.shu.miaosha.vo.LoginVO;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @date 2019/6/28 23:33
 */
@Service
public class MiaoshaUserService {
    private final MiaoshaUserDao miaoshaUserDao;

    public MiaoshaUserService(MiaoshaUserDao miaoshaUserDao) {
        this.miaoshaUserDao = miaoshaUserDao;
    }

    public MiaoshaUser getById(Long id) {
        return miaoshaUserDao.getById(id);
    }

    public CodeMsg login(LoginVO loginVO) {
        if (loginVO == null) {
            return CodeMsg.SERVER_ERROR;
        }
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            return CodeMsg.MOBILE_NOT_EXIST;
        }
        //验证密码
        String dbPass = user.getPassword();
        String salt = user.getSalt();
        String calcPass = MD5Util.formPassDbPass(password, salt);
        if (!calcPass.equals(dbPass)) {
            return CodeMsg.PASSWORD_ERROR;
        }
        return CodeMsg.SUCCESS;
    }
}
