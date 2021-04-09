package com.xiaozhuo.ctbsb.modules.answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuo.ctbsb.modules.answer.model.Answer;
import com.xiaozhuo.ctbsb.modules.answer.mapper.AnswerMapper;
import com.xiaozhuo.ctbsb.modules.answer.service.AnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjq
 * @since 2021-03-15
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {
    @Override
    public Answer uploadAnswer(String name, String text, String image, int upId, int questionId) {
        Answer answer = new Answer();
        answer.setName(name);
        answer.setText(text);
        answer.setImage(image);
        answer.setUpId(upId);
        answer.setQuestionId(questionId);
        answer.setCreatedDate(new Date());
        int insert = getBaseMapper().insert(answer);
        return (0<insert)?answer:null;
    }

    @Override
    public List<Answer> listByQuestionId(int questionId) {
        return getBaseMapper().selectList(new QueryWrapper<Answer>().lambda().eq(Answer::getQuestionId, questionId));
    }
}
