package org.example.dao.repo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.dao.mapper.UserInfoMapper;
import org.example.dao.po.UserInfoPo;
import org.example.vo.UserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mr.ahai
 */
@Component
public class UserInfoRepo {

    @Resource
    private UserInfoMapper userInfoMapper;
    public UserInfoPo getUserInfo(int id){
        LambdaQueryWrapper<UserInfoPo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserInfoPo::getId,id);
        return userInfoMapper.selectOne(lambdaQueryWrapper);
    }

    public void insertUserInfo(UserInfoVo userInfoVo){
        UserInfoPo userInfoPo = new UserInfoPo();
        BeanUtils.copyProperties(userInfoVo,userInfoPo);
        userInfoMapper.insert(userInfoPo);
    }
}
