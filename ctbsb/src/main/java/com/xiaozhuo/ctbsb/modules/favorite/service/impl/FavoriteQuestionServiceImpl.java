package com.xiaozhuo.ctbsb.modules.favorite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.modules.favorite.model.FavoriteQuestion;
import com.xiaozhuo.ctbsb.modules.favorite.mapper.FavoriteQuestionMapper;
import com.xiaozhuo.ctbsb.modules.favorite.service.FavoriteQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
public class FavoriteQuestionServiceImpl extends ServiceImpl<FavoriteQuestionMapper, FavoriteQuestion> implements FavoriteQuestionService {
    @Override
    public List<FavoriteQuestion> findAllQuestionByFavorite(int id) {
        QueryWrapper<FavoriteQuestion> favoriteQuestionQueryWrapper = new QueryWrapper<>();
        favoriteQuestionQueryWrapper.lambda().eq(FavoriteQuestion::getFavoriteId,id);
        return getBaseMapper().selectList(favoriteQuestionQueryWrapper);
    }

    @Override
    public boolean addFavoriteQuestion(int favoriteId, int questionId) {
        FavoriteQuestion favoriteQuestion = new FavoriteQuestion();
        favoriteQuestion.setFavoriteId(favoriteId);
        favoriteQuestion.setQuestionId(questionId);
        int insert = getBaseMapper().insert(favoriteQuestion);
        return 0 < insert;
    }

    @Override
    public boolean delete(int favoriteId, int questionId) {
        int delete = getBaseMapper().delete(new QueryWrapper<FavoriteQuestion>().lambda()
                .eq(FavoriteQuestion::getQuestionId, questionId).eq(FavoriteQuestion::getFavoriteId, favoriteId));
        return 0<delete;
    }

    @Override
    public Page<FavoriteQuestion> listByFavoriteId(int id, int pageNum, int pageSize) {
        Page<FavoriteQuestion> favoriteQuestionPage = new Page<>(pageNum, pageSize);
        return page(favoriteQuestionPage, new QueryWrapper<FavoriteQuestion>().lambda().eq(FavoriteQuestion::getFavoriteId, id));
    }

    @Override
    public int count(int favoriteId) {
        return getBaseMapper().selectCount(new QueryWrapper<FavoriteQuestion>().lambda().eq(FavoriteQuestion::getFavoriteId, favoriteId));
    }
}
