package com.waihai.usercenter.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waihai.usercenter.common.ErrorCode;
import com.waihai.usercenter.exception.BusinessException;
import com.waihai.usercenter.mapper.TeamMapper;
import com.waihai.usercenter.model.domin.Team;
import com.waihai.usercenter.model.domin.User;
import com.waihai.usercenter.model.enums.TeamStatusEnum;
import com.waihai.usercenter.service.TeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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
        //1. 请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //3. 校验信息
        //   1. 队伍人数 > 1 且 <= 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数设置错误");
        }

        //   2. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题过长");
        }

        //   3. 描述 <= 512
        String description = team.getDescription();
        if (!StringUtils.isBlank((description)) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }

        //   4. status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
        if (teamStatusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态设置错误");
        }

        //   5. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (teamStatusEnum == TeamStatusEnum.SECRET) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置错误");
            }
        }

        //   6. 当前时间 > 超时时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间 < 当前时间");
        }

        //   7. 校验用户最多创建 5 个队伍


        //4. 插入队伍信息到队伍表
        //5. 插入用户  => 队伍关系到关系表

        return 0;
    }
}




















