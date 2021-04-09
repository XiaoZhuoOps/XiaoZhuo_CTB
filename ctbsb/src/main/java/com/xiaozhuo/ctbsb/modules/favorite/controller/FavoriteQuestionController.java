package com.xiaozhuo.ctbsb.modules.favorite.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.common.Api.CommonPage;
import com.xiaozhuo.ctbsb.common.Api.CommonResult;
import com.xiaozhuo.ctbsb.common.exception.Asserts;
import com.xiaozhuo.ctbsb.domain.QAL;
import com.xiaozhuo.ctbsb.modules.favorite.model.Favorite;
import com.xiaozhuo.ctbsb.modules.favorite.model.FavoriteQuestion;
import com.xiaozhuo.ctbsb.modules.favorite.service.FavoriteQuestionService;
import com.xiaozhuo.ctbsb.modules.favorite.service.FavoriteService;
import com.xiaozhuo.ctbsb.modules.note.service.NoteService;
import com.xiaozhuo.ctbsb.modules.question.model.Question;
import com.xiaozhuo.ctbsb.modules.question.service.QuestionService;
import com.xiaozhuo.ctbsb.security.annotation.UserLoginToken;
import com.xiaozhuo.ctbsb.security.util.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjq
 * @since 2021-02-25
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteQuestionController {

    @Autowired
    FavoriteQuestionService favoriteQuestionService;

    @Autowired
    QuestionService questionService;

    @Autowired
    NoteService noteService;

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    //判断用户是否有权限操作错题本
    boolean checkFavAuth(int userId, int[] favoriteIds){
        for(int favoriteId:favoriteIds) {
            Favorite byId = favoriteService.findById(favoriteId);
            if(byId.getOwnerId()!=userId) return false;
            }
        return true;
    }

    @UserLoginToken
    @RequestMapping(value = "/listQuestionsByFavoriteId/{favoriteId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取错题本题目列表")
    public CommonPage<QAL> listQuestionsByFavoriteId(@PathVariable("favoriteId") int favoriteId,
                                                     @RequestParam("pageNum") int pageNum,
                                                     @RequestParam("pageSize") int pageSize){
        //返回page
        Page<FavoriteQuestion> favoriteQuestionPage = favoriteQuestionService.listByFavoriteId(favoriteId, pageNum, pageSize);
        List<FavoriteQuestion> records = favoriteQuestionPage.getRecords();
        List<Integer> ids = new ArrayList<>(records.size());
        for(FavoriteQuestion favoriteQuestion:records){
            ids.add(favoriteQuestion.getQuestionId());
        }
        Page<QAL> qalPage = questionService.listQALByIds(ids, pageNum, pageSize);
        qalPage.setTotal(favoriteQuestionPage.getTotal());
        return CommonPage.restPage(qalPage);
    }

    @UserLoginToken
    @RequestMapping(value = "/addQuestionToFavorite", method = RequestMethod.POST)
    @ApiOperation(value = "添加题目到错题本")
    public CommonResult<String> addQuestionToFavorite(@RequestParam("questionId") int questionId,
                                                      @RequestParam("favoriteIds") int[] favoriteIds,
                                                      @RequestParam("typeIds") int[] typeIds,
                                                      @RequestParam("difficultyId") int difficultyId,
                                                      @RequestParam("noteText") String text,
                                                      HttpServletRequest request){

        //check userId 和 favoriteId 是否对应
        int userId = (int) request.getAttribute("userId");
        if(!checkFavAuth(userId, favoriteIds)) return CommonResult.failed("不具有权限");
        for(int favoriteId: favoriteIds) {
            if(49 < favoriteQuestionService.count(favoriteId))
                return CommonResult.failed("最大题目数量超过50 请开通VIP");
        }
        boolean f = questionService.addQuestionToFavorite(userId, questionId, favoriteIds, typeIds, difficultyId, text);
        return CommonResult.success("添加成功");
    }

    @UserLoginToken
    @RequestMapping(value = "/deleteQuestion", method = RequestMethod.POST)
    @ApiOperation(value = "删除题目从错题本")
    public CommonResult<String> deleteQuestion(@RequestBody FavoriteQuestion favoriteQuestion,
                                               HttpServletRequest request){
        int userId = (int) request.getAttribute("userId");
        int[] favoriteIds = new int[1];
        favoriteIds[0] = favoriteQuestion.getFavoriteId();
        if(!checkFavAuth(userId,favoriteIds)) return CommonResult.failed("不具有权限");
        boolean delete = favoriteQuestionService.delete(favoriteQuestion.getFavoriteId(), favoriteQuestion.getQuestionId());
        if(delete) return CommonResult.success(null,"删除成功");
        else return CommonResult.failed("删除失败");
    }

    @UserLoginToken
    @RequestMapping(value = "/copyFavorite", method = RequestMethod.POST)
    @ApiOperation(value = "复制错题本")
    public CommonResult<String> copyFavorite(@RequestParam("qIds")  int[] qIds, @RequestParam("fIds") int[] fIds){
        boolean flag = questionService.copyQuestionToFavorite(qIds, fIds);
        if(flag) return CommonResult.success(null,"复制成功");
        else return CommonResult.failed("复制失败");
    }

}

