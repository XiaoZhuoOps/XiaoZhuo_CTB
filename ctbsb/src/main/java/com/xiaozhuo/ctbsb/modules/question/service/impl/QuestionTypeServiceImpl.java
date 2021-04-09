package com.xiaozhuo.ctbsb.modules.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuo.ctbsb.modules.question.model.QuestionType;
import com.xiaozhuo.ctbsb.modules.question.mapper.QuestionTypeMapper;
import com.xiaozhuo.ctbsb.modules.question.model.Type;
import com.xiaozhuo.ctbsb.modules.question.service.QuestionTypeService;
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
public class QuestionTypeServiceImpl extends ServiceImpl<QuestionTypeMapper, QuestionType> implements QuestionTypeService {
    @Override
    public List<Integer> listByQuestionId(int questionId) {
        List<QuestionType> questionTypes = getBaseMapper().selectList(new QueryWrapper<QuestionType>().lambda().eq(QuestionType::getQuestionId, questionId));
        List<Integer> typeIds = new ArrayList<>();
        for(QuestionType questionType:questionTypes){
            typeIds.add(questionType.getTypeId());
        }
    return typeIds;
    }

}
