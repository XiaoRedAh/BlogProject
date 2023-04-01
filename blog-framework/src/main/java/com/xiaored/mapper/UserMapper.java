package com.xiaored.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaored.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-10 18:43:49
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
