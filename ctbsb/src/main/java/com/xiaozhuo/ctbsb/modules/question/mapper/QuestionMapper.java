package com.xiaozhuo.ctbsb.modules.question.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.modules.question.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjq
 * @since 2021-02-16
 */
@Component(value = "QuestionMapper")
public interface QuestionMapper extends BaseMapper<Question> {
    Page<Question> selectByKw(Page<?> page, @Param("kw") String kw);
    List<Question> selectAll();
}
