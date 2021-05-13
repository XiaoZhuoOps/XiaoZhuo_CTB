package com.xiaozhuo.ctbsb.modules.question.mapper;

import com.xiaozhuo.ctbsb.modules.question.model.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhuo
 * @date 2021/4/20 15:45
 */
@Repository
public interface QuestionRepository extends ElasticsearchRepository<Question, Integer> {

}
