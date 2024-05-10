package com.waihai.usercenter.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waihai.usercenter.mapper.TeamMapper;
import com.waihai.usercenter.model.Team;
import com.waihai.usercenter.model.User;
import com.waihai.usercenter.service.TeamService;
import org.springframework.stereotype.Service;

/**
 * @author 外害
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-05-10 21:15:12
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {


    @Override
    public long addTeam(Team team, User loginUser) {
        return 0;
    }
}




