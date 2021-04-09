package com.xiaozhuo.ctbsb.modules.question.controller;


import com.xiaozhuo.ctbsb.common.Api.CommonResult;
import com.xiaozhuo.ctbsb.modules.question.model.Difficulty;
import com.xiaozhuo.ctbsb.modules.question.service.DifficultyService;
import com.xiaozhuo.ctbsb.security.annotation.UserLoginToken;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjq
 * @since 2021-02-17
 */
@RestController
@RequestMapping("/question/difficulty")
public class DifficultyController {

    @Autowired
    DifficultyService difficultyService;

    @UserLoginToken
    @ApiOperation(value = "获取所有难度等级")
    @RequestMapping(value = "/list", method = RequestMethod.GET)

    public CommonResult<List<Difficulty>> list(){
        List<Difficulty> list = difficultyService.list();
        return CommonResult.success(list,"返回成功");
    }
}

