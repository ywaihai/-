package com.waihai.usercenter.service;

import com.waihai.usercenter.model.domin.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.waihai.usercenter.model.domin.User;

/**
* @author y2054
* @description 针对表【notice(公告)】的数据库操作Service
* @createDate 2024-06-16 15:03:58
*/
public interface NoticeService extends IService<Notice> {

    int addNotice(Notice notice, User loginUser);

    boolean deleteNotice(long id);
}
