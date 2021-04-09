package com.xiaozhuo.ctbsb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaozhuo.ctbsb.modules.answer.model.Answer;
import com.xiaozhuo.ctbsb.modules.question.model.Difficulty;
import com.xiaozhuo.ctbsb.modules.question.model.Knowledge;
import com.xiaozhuo.ctbsb.modules.question.model.Question;
import com.xiaozhuo.ctbsb.modules.question.model.Type;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="QAL", description="question entity with answer and labels")
public class QAL implements Serializable {
    Question question;
    List<Answer> answers;
    Difficulty difficulty;
    List<Knowledge> knowledges;
    List<Type> types;

    public QAL(Question question, List<Answer> answers, Difficulty difficulty, List<Knowledge> knowledges, List<Type> types){
        this.question = question;
        this.answers = answers;
        this.difficulty = difficulty;
        this.knowledges = knowledges;
        this.types = types;
    }
}
