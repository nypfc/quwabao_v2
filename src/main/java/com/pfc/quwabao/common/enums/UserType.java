package com.pfc.quwabao.common.enums;

import lombok.Getter;

@Getter
public enum UserType {

    Level_0(0,"普通用户"),
    Level_Team(1,"团队长"),
    ;

    private int value;

    private String name;

    UserType(int value, String name){
        this.value = value;
        this.name = name;
    }

}
