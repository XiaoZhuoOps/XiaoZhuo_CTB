package com.xiaozhuo.ctbsb.modules.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuo.ctbsb.modules.question.model.QuestionDifficulty;
import com.xiaozhuo.ctbsb.modules.question.mapper.QuestionDifficultyMapper;
import com.xiaozhuo.ctbsb.modules.question.model.QuestionKnowledge;
import com.xiaozhuo.ctbsb.modules.question.service.QuestionDifficultyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjq
 * @since 2021-02-17
 */
@Service
public class QuestionDifficultyServiceImpl extends ServiceImpl<QuestionDifficultyMapper, QuestionDifficulty> implements QuestionDifficultyService {
    @Override
    public int findByQuestionId(int questionId) {
        QuestionDifficulty questionDifficulty = getBaseMapper().selectOne(new QueryWrapper<QuestionDifficulty>().lambda().eq(QuestionDifficulty::getQuestionId, questionId));
        return questionDifficulty.getDifficultyId();
    }
}
