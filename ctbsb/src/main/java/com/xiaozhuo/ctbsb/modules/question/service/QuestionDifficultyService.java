package com.xiaozhuo.ctbsb.modules.question.service;

import com.xiaozhuo.ctbsb.modules.question.model.QuestionDifficulty;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjq
 * @since 2021-02-17
 */
public interface QuestionDifficultyService extends IService<QuestionDifficulty> {
    int findByQuestionId(int questionId);
}
