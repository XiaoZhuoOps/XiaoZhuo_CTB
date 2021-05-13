package com.xiaozhuo.ctbsb.modules.question.service;

import com.xiaozhuo.ctbsb.modules.question.model.Question;
import org.springframework.data.domain.Page;

/**
 * @author xiaozhuo
 * @date 2021/4/20 15:48
 */
public interface ElasticsearchService {
    public void saveQuestion(Question question);
    public void deleteQuestion(int id);
    public Page<Question> searchQuestion(String kw, int pageNum, int pageSize);
}
