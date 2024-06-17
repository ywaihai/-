package com.waihai.usercenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.waihai.usercenter.common.BaseResponse;
import com.waihai.usercenter.common.ErrorCode;
import com.waihai.usercenter.common.ResultUtils;
import com.waihai.usercenter.exception.BusinessException;
import com.waihai.usercenter.model.domin.Notice;
import com.waihai.usercenter.model.domin.Team;
import com.waihai.usercenter.model.domin.User;
import com.waihai.usercenter.model.dto.NoticeQuery;
import com.waihai.usercenter.model.dto.TeamQuery;
import com.waihai.usercenter.model.request.DeleteRequest;
import com.waihai.usercenter.model.request.TeamAddRequest;
import com.waihai.usercenter.service.NoticeService;
import com.waihai.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@Slf4j
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public BaseResponse<Page<Notice>> listNotices(@ParameterObject NoticeQuery noticeQuery) {
        if (noticeQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<Notice> page = new Page<>(noticeQuery.getPageNum(), noticeQuery.getPageSize());
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        Page<Notice> resultPage = noticeService.page(page, queryWrapper);

        return ResultUtils.success(resultPage);
    }

    // TODO session过期刷新时间
    @PostMapping("/add")
    public BaseResponse<Integer> addNotice(@RequestBody Notice notice, HttpServletRequest request) {
        if (notice == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Integer noticeId = noticeService.addNotice(notice, loginUser);

        return ResultUtils.success(noticeId);
    }

    // TODO 优化写法
    @PostMapping("/update")
    public BaseResponse<Boolean> updateNotice(@RequestBody Notice notice, HttpServletRequest request) {
        if (notice == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.getLoginUser(request);
        Integer id = notice.getId();
        Notice byId = noticeService.getById(id);
        BeanUtils.copyProperties(notice, byId);
        boolean b = noticeService.updateById(byId);

        return ResultUtils.success(b);
    }

    // TODO 逻辑删除
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteNotice(@RequestParam Integer noticeId) {
        if (noticeId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = noticeService.deleteNotice(noticeId);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

}
