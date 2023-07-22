package com.lzj.facetoface.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.facetoface.model.domain.UserTeam;
import com.lzj.facetoface.service.UserTeamService;
import com.lzj.facetoface.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 14397
* @description 针对表【user_team(用户队伍关系表)】的数据库操作Service实现
* @createDate 2023-07-14 09:47:47
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




