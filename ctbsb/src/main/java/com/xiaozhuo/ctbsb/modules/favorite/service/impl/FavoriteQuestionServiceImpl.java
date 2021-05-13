package com.xiaozhuo.ctbsb.modules.favorite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.common.exception.ApiException;
import com.xiaozhuo.ctbsb.common.exception.Asserts;
import com.xiaozhuo.ctbsb.modules.favorite.model.FavoriteQuestion;
import com.xiaozhuo.ctbsb.modules.favorite.mapper.FavoriteQuestionMapper;
import com.xiaozhuo.ctbsb.modules.favorite.service.FavoriteQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(rollbackFor = ApiException.class)
public class FavoriteQuestionServiceImpl extends ServiceImpl<FavoriteQuestionMapper, FavoriteQuestion> implements FavoriteQuestionService {
    @Override
    public List<FavoriteQuestion> findAllQuestionByFavorite(int id) {
        QueryWrapper<FavoriteQuestion> favoriteQuestionQueryWrapper = new QueryWrapper<>();
        favoriteQuestionQueryWrapper.lambda().eq(FavoriteQuestion::getFavoriteId,id);
        return getBaseMapper().selectList(favoriteQuestionQueryWrapper);
    }

    @Override
    public boolean addFavoriteQuestion(int favoriteId, int questionId) {
        int insert = 0;
        try{
            FavoriteQuestion favoriteQuestion = new FavoriteQuestion();
            favoriteQuestion.setFavoriteId(favoriteId);
            favoriteQuestion.setQuestionId(questionId);
            insert = getBaseMapper().insert(favoriteQuestion);
        }catch (Exception e){
            Asserts.fail("添加失败");
        }
        return 0 < insert;
    }

    @Override
    public boolean delete(int favoriteId, int questionId) {
        int delete = 0;
        try{
            delete = getBaseMapper().delete(new QueryWrapper<FavoriteQuestion>().lambda()
                    .eq(FavoriteQuestion::getQuestionId, questionId).eq(FavoriteQuestion::getFavoriteId, favoriteId));
        }catch (Exception e){
            Asserts.fail("删除失败");
        }
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

    @Override
    public boolean copyQuestionToFavorite(int[] qIds, int[] fIds) {
        try{
            for(int qId : qIds){
                for (int fId : fIds){
                    FavoriteQuestion fq = new FavoriteQuestion();
                    fq.setQuestionId(qId);
                    fq.setFavoriteId(fId);
                    if(null != getBaseMapper().selectOne(new QueryWrapper<FavoriteQuestion>().lambda()
                            .eq(FavoriteQuestion::getFavoriteId, fId)
                            .eq(FavoriteQuestion::getQuestionId, qId))){
                        continue;
                    }
                    getBaseMapper().insert(fq);
                }
            }
        }catch (Exception e){
            Asserts.fail("复制失败");
        }
        return true;
    }
}
