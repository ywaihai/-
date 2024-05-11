package com.waihai.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.waihai.usercenter.model.domin.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author waihai
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-01-26 12:21:50
*/
public interface UserService extends IService<User> {
    /**
     *用户注册
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      请求
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    User getSafetyUser(User user);

    /**
     * 用户注销
     *
     * @param request 请求
     * @return Integer对象
     */
    Integer userLogout(HttpServletRequest request);

    /**
     * 根据标签查询用户
     *
     * @param tagNameList
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    int updateUser(User user, User loginUser);

    /**
     * 判断用户是否是管理员
     *
     * @param request 请求
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 判断用户是否是管理员
     *
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);

    /**
     * 获取当前用户信息
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);
}
