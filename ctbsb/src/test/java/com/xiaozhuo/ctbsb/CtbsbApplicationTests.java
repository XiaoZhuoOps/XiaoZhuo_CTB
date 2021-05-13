package com.xiaozhuo.ctbsb;

import com.xiaozhuo.ctbsb.common.utils.EmailUtil;
import com.xiaozhuo.ctbsb.modules.question.mapper.QuestionRepository;
import com.xiaozhuo.ctbsb.modules.question.model.Question;
import com.xiaozhuo.ctbsb.modules.question.service.ElasticsearchService;
import com.xiaozhuo.ctbsb.modules.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class CtbsbApplicationTests {

    static private Log log = LogFactory.getLog(CtbsbApplicationTests.class);

    @Autowired
    private ElasticsearchTemplate elasticTemplate;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    EmailUtil emailUtil;

    @Test
    void test1(){
        elasticTemplate.createIndex(Question.class);
        elasticTemplate.putMapping(Question.class);
    }

    @Test
    void test2(){
        Question question = new Question();
        question.setId(1);
        question.setName("测试");
        question.setText("<h1 class=\"QuestionHeader-title\" style=\"margin: 12px 0px 4px; font-variant-numeric: inherit; font-variant-east-asian: inherit; font-stretch: inherit; font-size: 22px; line-height: 32px; font-family: -apple-system, BlinkMacSystemFont, &quot;Helvetica Neue&quot;, &quot;PingFang SC&quot;, &quot;Microsoft YaHei&quot;, &quot;Source Han Sans SC&quot;, &quot;Noto Sans CJK SC&quot;, " +
                "&quot;WenQuanYi Micro Hei&quot;, sans-serif; color: rgb(18, 18, 18); white-space: normal;\">印度疫情失控，会影响到中国吗？</h1>");
        question.setCreatedDate(new Date());
        question.setUpId(1);
        question.setImage("null");
        elasticsearchService.saveQuestion(question);
    }

    @Test
    void test3(){
        Page<Question> questions = elasticsearchService.searchQuestion("失控", 1, 10);
        for(Question question:questions){
            log.info(question.getText());
        }
    }

    @Test
    void test4(){
        emailUtil.sendActivateMail("mail.html", "xiaozhuoops@outlook.com", "1234");
    }
}
