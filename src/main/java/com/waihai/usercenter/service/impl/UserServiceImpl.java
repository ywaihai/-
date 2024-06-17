package com.waihai.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waihai.usercenter.common.ErrorCode;
import com.waihai.usercenter.exception.BusinessException;
import com.waihai.usercenter.mapper.UserMapper;
import com.waihai.usercenter.model.domin.User;
import com.waihai.usercenter.service.UserService;
import com.waihai.usercenter.utils.AlgorithmUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.waihai.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.waihai.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
 * @author waihai
 * @description 用户服务实现类
 * @createDate 2024-1-27
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    /**
     * 盐值
     */
    private static final String SALT = "waihai";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1. 校验字符串是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 校验用户账号长度
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 3. 校验密码长度
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 4. 校验星球编号长度
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星期编号过长");
        }
        // 4. 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 5. 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        // 5. 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        // 6. 密码与校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 7. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 8. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验字符串是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        // 2. 校验用户账号长度
        if (userAccount.length() < 4) {
            return null;
        }
        // 3. 校验密码长度
        if (userPassword.length() < 8) {
            return null;
        }
        // 4. 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        // 5. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 6. 校验密码是否输入正确
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        // 7. 脱敏
        User saftyUser = getSafetyUser(user);
        // 8. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, saftyUser);

        return saftyUser;
    }

    /**
     * 用户脱敏
     *
     * @param orginUser 用户信息
     * @return
     */
    @Override
    public User getSafetyUser(User orginUser) {
        if (orginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        User saftyUser = new User();
        saftyUser.setId(orginUser.getId());
        saftyUser.setUsername(orginUser.getUsername());
        saftyUser.setUserAccount(orginUser.getUserAccount());
        saftyUser.setAvatarUrl(orginUser.getAvatarUrl());
        saftyUser.setGender(orginUser.getGender());
        saftyUser.setProfile(orginUser.getProfile());
        saftyUser.setPhone(orginUser.getPhone());
        saftyUser.setEmail(orginUser.getEmail());
        saftyUser.setUserStatus(orginUser.getUserStatus());
        saftyUser.setCreateTime(orginUser.getCreateTime());
        saftyUser.setUserRole(orginUser.getUserRole());
        saftyUser.setPlanetCode(orginUser.getPlanetCode());
        saftyUser.setTags(orginUser.getTags());
        return saftyUser;
    }

    /**
     * 用户注销
     *
     * @param request 请求
     * @return Integer对象
     */
    @Override
    public Integer userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

//    /**
//     * 根据标签搜索用户（sql实现）
//     *
//     * @param tagNameList
//     * @return
//     */
//    @Override
//    public List<User> searchUsersByTags(List<String> tagNameList) {
//        //非空，不然会暴露所有数据
//        if (CollectionUtils.isEmpty(tagNameList)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        //拼接tag
//        // like '%Java%' and like '%Python%'
//        for (String tagList : tagNameList) {
//            queryWrapper = queryWrapper.like("tags", tagList);
//        }
//        List<User> userList = userMapper.selectList(queryWrapper);
//        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());

    /**
     * 根据标签查询所有用户（内存）
     *
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {
        // 非空防止暴露所有数据
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.查询出所有用户并存储
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        // 2.判断内存中是否包含要求的标签
        return userList.stream().filter(user -> {
            String tagStr = user.getTags();
            if (StringUtils.isBlank(tagStr)) {
                return false;
            }
            Set<String> tempTagNameSet = gson.fromJson(tagStr, new TypeToken<Set<String>>() {
            }.getType());
            // 利用java8特性判断 tempTagNameSet 是否为空
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @param loginUser
     * @return
     */
    @Override
    public int updateUser(User user, User loginUser) {
        long userId = user.getId();
        if (userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // todo 补充校验，如果用户没有传任何要更新的值，就直接报错，不用执行 update 语句
        // 如果是管理员，允许更新任意用户
        // 如果不是管理员，只允许更新当前（自己的）信息
        if (!isAdmin(loginUser) && userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 判断用户是否是管理员
     *
     * @param request 请求
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 判断用户是否是管理员
     *
     * @param loginUser
     * @return
     */
    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 获取当前用户信息
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return (User) userObj;
    }

    /**
     * 获取最匹配的用户
     *
     * @param num
     * @param loginUser
     * @return
     */
    @Override
    public List<User> matchUsers(long num, User loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tags");//我们只查id和tags字段
        queryWrapper.isNotNull("tags");
        //获取标签不为空的所有用户的列表
        List<User> userList = this.list(queryWrapper);
        //获取当前登录用户的标签
        String tags = loginUser.getTags();
        //tags是json格式，现在转为java对象
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        //记录用户的下标和相似度
        List<Pair<User, Long>> list = new ArrayList<>();
        //依次计算所有用户和当前用户的相似度
        for (User user : userList) {
            //获取列表用户的标签
            String userTags = user.getTags();
            //用户没有标签或者遍历到自己，就遍历下一个用户
            if (StringUtils.isBlank(userTags) || user.getId().equals(loginUser.getId())) {
                continue;
            }
            //将用户的标签转为java对象
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            //两两比较,获取相识度，相似度越高，就越匹配
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            //记录
            list.add(new Pair<>(user, distance));
        }
        // 按编辑距离由小到大排序,升序，编辑距离越短，匹配度越高，即相识度越高
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(num)
                .toList();
        //从topUserPairList取出用户,这里的用户只有id和tags信息,这里已经根据相似度排好序了
        List<Long> userIdList = topUserPairList.stream().map(pair ->
                pair.getKey().getId()).collect(Collectors.toList());
        //获取用户的所有信息，并进行脱敏，得到的是未排序的用户
        // 1, 3, 2
        // User1、User2、User3
        // 1 => User1, 2 => User2, 3 => User3
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIdList);//使用in了之后就又打乱了顺序
        Map<Long, List<User>> userIdUserListMap = this.list(userQueryWrapper).
                stream().
                map(this::getSafetyUser).
                collect(Collectors.groupingBy(User::getId));
        //重新排序
        List<User> finalUserList = new ArrayList<>();
        for (Long userId : userIdList) {
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        return finalUserList;
    }

}
