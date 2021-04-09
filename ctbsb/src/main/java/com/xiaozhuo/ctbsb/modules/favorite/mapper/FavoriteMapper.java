package com.xiaozhuo.ctbsb.modules.favorite.mapper;

import com.xiaozhuo.ctbsb.modules.favorite.model.Favorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjq
 * @since 2021-02-25
 */
public interface FavoriteMapper extends BaseMapper<Favorite> {
    List<Favorite> selectFavoriteByQuestion(@Param("userId") int userId, @Param("questionId") int questionId);
}
