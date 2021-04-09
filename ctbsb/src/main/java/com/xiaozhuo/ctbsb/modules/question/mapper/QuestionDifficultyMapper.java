package com.xiaozhuo.ctbsb.modules.question.mapper;

import com.xiaozhuo.ctbsb.modules.question.model.QuestionDifficulty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjq
 * @since 2021-02-17
 */
@Component(value = "QuestionDifficultyMapper")
public interface QuestionDifficultyMapper extends BaseMapper<QuestionDifficulty> {

}
