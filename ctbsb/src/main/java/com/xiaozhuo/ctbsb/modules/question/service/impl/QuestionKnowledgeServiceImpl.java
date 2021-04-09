package com.xiaozhuo.ctbsb.modules.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuo.ctbsb.modules.question.model.Question;
import com.xiaozhuo.ctbsb.modules.question.model.QuestionKnowledge;
import com.xiaozhuo.ctbsb.modules.question.mapper.QuestionKnowledgeMapper;
import com.xiaozhuo.ctbsb.modules.question.service.QuestionKnowledgeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjq
 * @since 2021-02-17
 */
@Service
public class QuestionKnowledgeServiceImpl extends ServiceImpl<QuestionKnowledgeMapper, QuestionKnowledge> implements QuestionKnowledgeService {
    @Override
    public boolean addKnowledgeLabel(int questionId, int[] knowledgeIds) {
        ArrayList<QuestionKnowledge> questionKnowledges = new ArrayList<>();
        for (int knowledgeId : knowledgeIds) {
            QuestionKnowledge questionKnowledge = new QuestionKnowledge();
            questionKnowledge.setQuestionId(questionId);
            questionKnowledge.setKnowledgeId(knowledgeId);
            questionKnowledges.add(questionKnowledge);
        }
        return saveBatch(questionKnowledges, knowledgeIds.length);
    }

    @Override
    public List<Integer> listByQuestionId(int questionId) {
        List<QuestionKnowledge> questionKnowledges = getBaseMapper().selectList(new QueryWrapper<QuestionKnowledge>().lambda().eq(QuestionKnowledge::getKnowledgeId, questionId));
        List<Integer> knowledgeIds = new ArrayList<>();
        for(QuestionKnowledge questionKnowledge:questionKnowledges){
            knowledgeIds.add(questionKnowledge.getKnowledgeId());
        }
        return knowledgeIds;
    }
}
