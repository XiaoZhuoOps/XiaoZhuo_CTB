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
 * @since 2021-02-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("difficulty")
@ApiModel(value="Difficulty对象", description="")
public class Difficulty implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "难度级别 对应关系可自定义")
    private String grade;
}
