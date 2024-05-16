package com.waihai.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waihai.usercenter.model.domin.Team;
import com.waihai.usercenter.model.domin.User;
import com.waihai.usercenter.model.dto.TeamQuery;
import com.waihai.usercenter.model.vo.TeamUserVO;

import java.util.List;

/**
* @author 外害
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-05-10 21:15:12
*/
public interface TeamService extends IService<Team> {

    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);
}
