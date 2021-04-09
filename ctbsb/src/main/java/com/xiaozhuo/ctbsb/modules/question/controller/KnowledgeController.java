package com.xiaozhuo.ctbsb.modules.question.controller;


import com.xiaozhuo.ctbsb.common.Api.CommonResult;
import com.xiaozhuo.ctbsb.modules.question.model.Knowledge;
import com.xiaozhuo.ctbsb.modules.question.service.KnowledgeService;
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
 * @since 2021-03-24
 */
@RestController
@RequestMapping("/question/knowledge")
public class KnowledgeController {
    @Autowired
    KnowledgeService knowledgeService;

    @UserLoginToken
    @ApiOperation(value = "获取所有知识点")
    @RequestMapping(value = "/list", method = RequestMethod.GET)

    public CommonResult<List<Knowledge>> list(){
        List<Knowledge> list = knowledgeService.list();
        return CommonResult.success(list,"返回成功");
    }
}

