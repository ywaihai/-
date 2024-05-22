package com.waihai.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waihai.usercenter.model.domin.Team;
import com.waihai.usercenter.model.domin.User;
import com.waihai.usercenter.model.dto.TeamQuery;
import com.waihai.usercenter.model.request.TeamJoinRequest;
import com.waihai.usercenter.model.request.TeamQuitRequest;
import com.waihai.usercenter.model.request.TeamUpdateRequest;
import com.waihai.usercenter.model.vo.TeamUserVO;

import java.util.List;

/**
* @author 外害
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-05-10 21:15:12
*/
public interface TeamService extends IService<Team> {

    /**
     * 查询队伍
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 更新队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest team, User loginUser);

    /**
     * 加入队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest team, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 解散队伍
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTeam(long id, User loginUser);
}
