package com.xiaozhuo.ctbsb.modules.favorite.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hjq
 * @since 2021-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("favorite_question")
@ApiModel(value="FavoriteQuestion对象", description="")
public class FavoriteQuestion implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer favoriteId;

    private Integer questionId;


}
