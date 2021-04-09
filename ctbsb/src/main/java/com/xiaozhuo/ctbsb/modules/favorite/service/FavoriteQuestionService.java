package com.xiaozhuo.ctbsb.modules.favorite.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.modules.favorite.model.FavoriteQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjq
 * @since 2021-02-25
 */
public interface FavoriteQuestionService extends IService<FavoriteQuestion> {
    Page<FavoriteQuestion> listByFavoriteId(int id, int pageNum, int pageSize);
    List<FavoriteQuestion> findAllQuestionByFavorite(int id);
    boolean addFavoriteQuestion(int favoriteId, int questionId);
    boolean delete(int favoriteId, int questionId);
    int count(int favoriteId);
}
