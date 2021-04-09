package com.xiaozhuo.ctbsb.modules.question.mapper;

import com.xiaozhuo.ctbsb.modules.question.model.Knowledge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjq
 * @since 2021-03-24
 */
public interface KnowledgeMapper extends BaseMapper<Knowledge> {
    List<Knowledge> selectByQuestionId(@Param("questionId") int questionId);
}
