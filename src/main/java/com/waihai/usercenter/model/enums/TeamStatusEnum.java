package com.waihai.usercenter.model.enums;

import com.waihai.usercenter.common.ErrorCode;
import com.waihai.usercenter.exception.BusinessException;
import com.waihai.usercenter.service.TeamService;

public enum TeamStatusEnum {
    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SECRET(2, "加密");

    private int value;

    private String status;

    TeamStatusEnum(int value, String status) {
        this.value = value;
        this.status = status;
    }

    public static TeamStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取一个包含所有 TeamStatusEnum 枚举常量的数组。
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            // 调用生成的 getter 方法获取 value
            if (value == teamStatusEnum.getValue()) {
                return teamStatusEnum;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getStatus() {
        return status;
    }
}