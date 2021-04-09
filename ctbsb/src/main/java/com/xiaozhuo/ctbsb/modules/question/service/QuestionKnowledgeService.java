package com.xiaozhuo.ctbsb.modules.question.service;

import com.xiaozhuo.ctbsb.modules.question.model.QuestionKnowledge;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjq
 * @since 2021-02-17
 */
public interface QuestionKnowledgeService extends IService<QuestionKnowledge> {
    boolean addKnowledgeLabel(int questionId, int[] knowledgeIds);
    List<Integer> listByQuestionId(int questionId);
}
