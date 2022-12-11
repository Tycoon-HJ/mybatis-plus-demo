package org.example.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.dao.po.UserInfoPo;

/**
 * @author mr.ahai
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoPo> {

}
