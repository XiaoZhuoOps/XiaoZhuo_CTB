package com.xiaozhuo.ctbsb.modules.answer.service;

import com.xiaozhuo.ctbsb.modules.answer.model.Answer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjq
 * @since 2021-03-15
 */
public interface AnswerService extends IService<Answer> {
    public Answer uploadAnswer(String name, String text, String image, int upId, int questionId);
    public List<Answer> listByQuestionId(int questionId);
}
