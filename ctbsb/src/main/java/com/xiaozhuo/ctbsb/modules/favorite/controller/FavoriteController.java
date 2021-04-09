package com.xiaozhuo.ctbsb.modules.favorite.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.common.Api.CommonPage;
import com.xiaozhuo.ctbsb.common.Api.CommonResult;
import com.xiaozhuo.ctbsb.modules.favorite.model.Favorite;
import com.xiaozhuo.ctbsb.modules.favorite.service.FavoriteService;
import com.xiaozhuo.ctbsb.security.annotation.UserLoginToken;
import com.xiaozhuo.ctbsb.security.util.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
public class FavoriteController {

    @Autowired
    FavoriteService favoriteService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @UserLoginToken
    @RequestMapping(value = "/addFavorite", method = RequestMethod.POST)
    @ApiOperation(value = "添加错题本")
    public CommonResult<Favorite> addFavorite(@RequestBody Favorite favoriteParam,
                                              HttpServletRequest request){
        int userId = (int) request.getAttribute("userId");
        //先判断该用户的错题本数目是否超过最大数目
        if(14 < favoriteService.count(userId)) return CommonResult.failed("错题本数目已超过15个，请开通VIP");
        Favorite favorite = favoriteService.addFavorite(favoriteParam.getName(), userId);
        if(favorite!=null) return CommonResult.success(favorite,"添加成功");
        else return CommonResult.failed("添加失败");
    }

    @UserLoginToken
    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    @ApiOperation(value = "重命名")
    public CommonResult<Favorite> rename(@RequestBody Favorite favoriteParam){
        Favorite favorite = favoriteService.updateFavorite(favoriteParam.getId(), favoriteParam.getName());
        return CommonResult.success(favorite,"修改成功");
    }

    @UserLoginToken
    @RequestMapping(value = "/listByUserId", method = RequestMethod.GET)
    @ApiOperation(value = "获取错题本列表")
    public CommonPage<Favorite> listAll(@RequestParam("pageNum") int pageNum,
                                        @RequestParam("pageSize") int pageSize,
                                        HttpServletRequest request){
        int userId = (int) request.getAttribute("userId");
        Page<Favorite> favoritePage = favoriteService.listByUserId(userId, pageNum, pageSize);
        return CommonPage.restPage(favoritePage);
    }

    @UserLoginToken
    @RequestMapping(value = "/listFavoritesByQuestionId/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "获取当前题目所在错题本")
    public CommonResult<List<Favorite>> listFavoritesByQuestionId(@PathVariable("id") int questionId,
                                        HttpServletRequest request){
        List<Favorite> favorites = favoriteService.listFavoritesByQuestionId((Integer) request.getAttribute("userId"), questionId);
        return CommonResult.success(favorites,"返回成功");
    }

    @UserLoginToken
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "删除错题本")
    public CommonResult<String> delete(@PathVariable("id") int id){
        boolean res = favoriteService.delete(id);
        if(res) return CommonResult.success(null,"删除成功");
        else return CommonResult.failed("删除失败");
    }

}

