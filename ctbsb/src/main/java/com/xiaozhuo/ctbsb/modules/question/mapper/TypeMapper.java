package com.xiaozhuo.ctbsb.modules.question.mapper;

import com.xiaozhuo.ctbsb.modules.question.model.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface TypeMapper extends BaseMapper<Type> {
    List<Type> selectByQuestionId(@Param("questionId") int questionId, @Param("userId") int userId);
}
