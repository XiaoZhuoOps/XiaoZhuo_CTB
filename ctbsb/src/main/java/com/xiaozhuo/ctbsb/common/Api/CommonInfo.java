package com.xiaozhuo.ctbsb.common.Api;

import lombok.Getter;
import lombok.Setter;

/**
 * wrap the information form service to controller
 */
@Getter
@Setter
public class CommonInfo {
    long code;
    String msg;

    public CommonInfo(long code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
