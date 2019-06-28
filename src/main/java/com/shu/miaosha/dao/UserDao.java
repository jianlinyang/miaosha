package com.shu.miaosha.dao;

import com.shu.miaosha.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author yang
 * @date 2019/6/27 23:44
 */
@Mapper
public interface UserDao {
    /**
     * 通过id查找
     *
     * @param id id
     * @return {@link User}
     */
    @Select("select * from user where id = #{id}")
    User getById(@Param("id") Integer id);
}
