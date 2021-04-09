package com.xiaozhuo.ctbsb.modules.favorite.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.modules.favorite.model.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjq
 * @since 2021-02-25
 */
public interface FavoriteService extends IService<Favorite> {
    Favorite addFavorite(String name, int ownerId);
    Favorite updateFavorite(int id, String newName);
    Favorite findById(int favoriteId);
    Page<Favorite> listByUserId(int userId, int pageNum, int pageSize);
    List<Favorite> listFavoritesByQuestionId(int userId, int questionId);
    boolean delete(int id);
    int count(int userId);

}
