package com.waihai.usercenter.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.waihai.usercenter.common.PageRequest;
import lombok.Data;

/**
 * 公告
 * @TableName notice
 */
@TableName(value ="notice")
@Data
public class NoticeQuery extends PageRequest {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}