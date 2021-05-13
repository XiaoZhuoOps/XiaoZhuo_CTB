package com.xiaozhuo.ctbsb.modules.question.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.domain.QAL;
import com.xiaozhuo.ctbsb.modules.favorite.model.Favorite;
import com.xiaozhuo.ctbsb.modules.question.model.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjq
 * @since 2021-02-16
 */
public interface QuestionService extends IService<Question> {
    Question findQuestionByName(String name);
    Question uploadQuestion(String name, String text, int uploadId, String questionImagePath);
    Page<Question> listQuestionByKeyword(String keyword, int pageNum, int pageSize);
    Page<Question> listQuestionByIds(List<Integer> ids, int pageNum, int pageSize);
    Question findQuestionById(int id);
    List<Question> listQuestion();
    //多表查询
    boolean addLabels(int questionId, int userId, int[] typeIds, int difficultyId);
    boolean deleteLabels(int questionId, int userId);
    QAL findQALById(int questionId, int userId);
    Page<QAL> listQALByIds(List<Integer> ids, int userId, int pageNum, int pageSize);
    boolean addQuestionToFavorite(int userId, int questionId, int[] favoriteIds ,int[] typeIds,int difficultyId, String text);
}

