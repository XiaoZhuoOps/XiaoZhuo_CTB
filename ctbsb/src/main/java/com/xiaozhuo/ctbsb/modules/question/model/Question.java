package com.xiaozhuo.ctbsb.modules.question.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * <p>
 * 
 * </p>
 *
 * @author hjq
 * @since 2021-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("question")
@ApiModel(value="Question对象", description="")
@Document(indexName = "ctb", type = "question")
public class Question implements Serializable {

    private static final long serialVersionUID=1L;

    @Id
    @Field(store=true, index = false, type = FieldType.Integer)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Field(store=true, type = FieldType.Text,
            analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String name;

    @Field(store=true, index = false, type = FieldType.Text)
    private String image;

    @Field(store=true, type = FieldType.Text,
            analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String text;

    @Field(store=true, index = false, type = FieldType.Integer)
    @ApiModelProperty(value = "上传用户id")
    private Integer upId;

    @Field(store=true, index = false, type = FieldType.Date)
    @ApiModelProperty(value = "创建日期")
    private Date createdDate;

    //2021-5-3新增字段 用于查询
    @Field(store=true, type = FieldType.Text,
            analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String plainText;
}
