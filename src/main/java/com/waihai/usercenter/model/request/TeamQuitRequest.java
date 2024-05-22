package com.waihai.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户退出队伍请求体
 */
@Data
public class TeamQuitRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -7167305644264889359L;

    /**
     * 队伍ID
     */
    private Long teamId;
}
