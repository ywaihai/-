package com.waihai.usercenter.service;

import com.waihai.usercenter.model.enums.TeamStatusEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

public class DraftTest {

    @Resource
    private TeamStatusEnum teamStatusEnum;

    @Test
    public void test(){
        TeamStatusEnum t = TeamStatusEnum.getEnumByValue(1);
        System.out.println(t);
    }
}
