package com.waihai.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waihai.usercenter.model.Team;
import com.waihai.usercenter.model.User;

/**
* @author 外害
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-05-10 21:15:12
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);
}
