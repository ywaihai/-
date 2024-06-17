package com.waihai.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.waihai.usercenter.common.BaseResponse;
import com.waihai.usercenter.common.ErrorCode;
import com.waihai.usercenter.common.ResultUtils;
import com.waihai.usercenter.exception.BusinessException;
import com.waihai.usercenter.model.domin.User;
import com.waihai.usercenter.model.request.UserLoginRequest;
import com.waihai.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Resource
    private UserService userService;

    /**
     * 管理员登录
     *
     * @param userLoginRequest 登录对象
     * @return 脱敏用户信息
     */
    @PostMapping("/login")
    public BaseResponse<User> adminLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userRole", 1);
        List<User> list = userService.list(queryWrapper);
        if (list == null || list.size() == 0) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

}
