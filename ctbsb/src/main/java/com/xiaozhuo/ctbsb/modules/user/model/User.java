package com.xiaozhuo.ctbsb.modules.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2021-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String password;

    private String email;

    private Long phone;

    @ApiModelProperty(value = "唯一激活码")
    @TableField("activate_Code")
    private String activateCode;

    @ApiModelProperty(value = "激活状态 0未激活 1激活")
    private Integer status;

    @ApiModelProperty(value = "头像文件路径 默认为空")
    private String avatar;

    @ApiModelProperty(value = "积分")
    private Integer credits;

    @ApiModelProperty(value = "注册日期")
    private Date createdDate;


}
