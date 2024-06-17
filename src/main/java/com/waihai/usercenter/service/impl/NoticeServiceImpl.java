package com.waihai.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waihai.usercenter.common.ErrorCode;
import com.waihai.usercenter.exception.BusinessException;
import com.waihai.usercenter.model.domin.Notice;
import com.waihai.usercenter.model.domin.User;
import com.waihai.usercenter.service.NoticeService;
import com.waihai.usercenter.mapper.NoticeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author y2054
* @description 针对表【notice(公告)】的数据库操作Service实现
* @createDate 2024-06-16 15:03:58
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

    @Override
    public int addNotice(Notice notice, User loginUser) {
        // 1. 请求参数是否为空？
        if (notice == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 3.标题与内容都不为空
        String title = notice.getTitle();
        String content = notice.getContent();
        if (StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 4.设置创建人
        String username = loginUser.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        notice.setCreateTime(new Date());
        notice.setCreateUser(username);

        boolean save = this.save(notice);
        if (!save) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return notice.getId();
    }

    @Override
    public boolean deleteNotice(long id) {

        boolean b = this.removeById(id);
        return b;
    }
}




