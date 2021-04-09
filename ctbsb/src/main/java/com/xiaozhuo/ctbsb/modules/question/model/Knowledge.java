package com.xiaozhuo.ctbsb.modules.question.model;

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
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("knowledge")
@ApiModel(value="Knowledge对象", description="")
public class Knowledge implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String kid;

    @ApiModelProperty(value = "知识点名 拼音")
    private String name;

}
