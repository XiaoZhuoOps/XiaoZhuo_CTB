package com.xiaozhuo.ctbsb.modules.note.controller;


import com.xiaozhuo.ctbsb.common.Api.CommonResult;
import com.xiaozhuo.ctbsb.modules.note.model.Note;
import com.xiaozhuo.ctbsb.modules.note.service.NoteService;
import com.xiaozhuo.ctbsb.modules.question.model.Knowledge;
import com.xiaozhuo.ctbsb.security.annotation.UserLoginToken;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjq
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    NoteService noteService;

    @UserLoginToken
    @ApiOperation(value = "查询笔记")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<Note> list(@RequestParam("favoriteId") int favoriteId,
                                   @RequestParam("questionId") int questionId){
        Note note = noteService.find(favoriteId, questionId);
        return CommonResult.success(note,"返回成功");
    }
}

