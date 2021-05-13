package com.xiaozhuo.ctbsb.modules.question.mapper;

import com.xiaozhuo.ctbsb.modules.question.model.Difficulty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaozhuo.ctbsb.modules.question.model.Type;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjq
 * @since 2021-02-17
 */
public interface DifficultyMapper extends BaseMapper<Difficulty> {
    List<Difficulty> selectByQuestionId(@Param("questionId") int questionId, @Param("userId") int userId);
}
