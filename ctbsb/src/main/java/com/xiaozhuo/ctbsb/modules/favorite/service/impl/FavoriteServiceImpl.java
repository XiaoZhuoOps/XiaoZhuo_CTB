package com.xiaozhuo.ctbsb.modules.favorite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.common.exception.Asserts;
import com.xiaozhuo.ctbsb.modules.favorite.model.Favorite;
import com.xiaozhuo.ctbsb.modules.favorite.mapper.FavoriteMapper;
import com.xiaozhuo.ctbsb.modules.favorite.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjq
 * @since 2021-02-25
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    @Override
    public Favorite addFavorite(String name, int ownerId) {
        Favorite favorite1 = getBaseMapper().selectOne(new QueryWrapper<Favorite>().lambda().eq(Favorite::getName, name).eq(Favorite::getOwnerId, ownerId));
        if(favorite1!=null) Asserts.fail("名称已存在");
        //
        Favorite favorite = new Favorite();
        favorite.setName(name);
        favorite.setOwnerId(ownerId);
        favorite.setCreatedDate(new Date());
        int insert = getBaseMapper().insert(favorite);
        if(0 < insert) return favorite;
        else return null;
    }

    @Override
    public Favorite updateFavorite(int id, String newName) {
        Favorite favorite = getBaseMapper().selectById(id);
        if(favorite == null) Asserts.fail("该ID不存在");
        favorite.setName(newName);
        int update = getBaseMapper().updateById(favorite);
        if(!(0 < update)) Asserts.fail("修改失败");
        return favorite;
    }

    @Override
    public Page<Favorite> listByUserId(int userId, int pageNum, int pageSize) {
        Page<Favorite> favoritePage = new Page<>(pageNum,pageSize);
        QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<Favorite>();
        favoriteQueryWrapper.lambda().eq(Favorite::getOwnerId, userId).orderByDesc(Favorite::getCreatedDate);
        return page(favoritePage, favoriteQueryWrapper);
    }

    @Override
    public List<Favorite> listFavoritesByQuestionId(int userId, int questionId) {
        return getBaseMapper().selectFavoriteByQuestion(userId, questionId);
    }

    @Override
    public boolean delete(int id) {
        int i = getBaseMapper().deleteById(id);
        return 0<i;
    }

    @Override
    public int count(int userId) {
        return getBaseMapper().selectCount(new QueryWrapper<Favorite>().lambda().eq(Favorite::getOwnerId, userId));
    }

    @Override
    public Favorite findById(int favoriteId) {
        return getBaseMapper().selectById(favoriteId);
    }
}
