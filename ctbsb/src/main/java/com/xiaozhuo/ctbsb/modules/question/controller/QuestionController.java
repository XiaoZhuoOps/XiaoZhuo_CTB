package com.xiaozhuo.ctbsb.modules.question.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.common.Api.CommonPage;
import com.xiaozhuo.ctbsb.common.Api.CommonResult;
import com.xiaozhuo.ctbsb.common.utils.OcrUtil;
import com.xiaozhuo.ctbsb.domain.QAL;
import com.xiaozhuo.ctbsb.modules.answer.model.Answer;
import com.xiaozhuo.ctbsb.modules.answer.service.AnswerService;
import com.xiaozhuo.ctbsb.modules.question.model.Difficulty;
import com.xiaozhuo.ctbsb.modules.question.model.Knowledge;
import com.xiaozhuo.ctbsb.modules.question.model.Question;
import com.xiaozhuo.ctbsb.modules.question.model.Type;
import com.xiaozhuo.ctbsb.modules.question.service.*;
import com.xiaozhuo.ctbsb.security.annotation.UserLoginToken;
import com.xiaozhuo.ctbsb.security.util.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjq
 * @since 2021-02-16
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionKnowledgeService questionKnowledgeService;

    @Autowired
    QuestionTypeService questionTypeService;

    @Autowired
    QuestionDifficultyService questionDifficultyService;

    @Autowired
    AnswerService answerService;

    @Autowired
    DifficultyService difficultyService;

    @Autowired
    TypeService typeService;

    @Autowired
    KnowledgeService knowledgeService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Value("${file-save-path}")
    String fileSavePath;

    @UserLoginToken
    @ApiOperation(value = "上传图片")
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public CommonResult<String> uploadImg(@RequestParam("uploadFile") MultipartFile uploadFile,
                                           HttpServletRequest request){
        int userId = (int) request.getAttribute("userId");
        String trueFilePath = null;
        String relativeDistPath =  "/question/"+ userId;
        String fileName = "/" + new Date().getTime() + ".jpg";
        File trueDistPath = new File(fileSavePath + relativeDistPath);
        if(uploadFile != null){
            try{
                if(!trueDistPath.isDirectory()){
                    boolean mkdir = trueDistPath.mkdirs();
                }
                trueFilePath = trueDistPath + fileName;
                File questionFile = new File(trueFilePath);
                uploadFile.transferTo(questionFile);
                return CommonResult.success(relativeDistPath + fileName);
            }catch (Exception e){
                return CommonResult.failed("图片上传失败");
            }
        }
        return CommonResult.failed("图片上传失败");
    }

    @UserLoginToken
    @ApiOperation(value = "上传题目")
    @RequestMapping(value = "/uploadQuestion", method = RequestMethod.POST)
    public CommonResult<Question> uploadQuestion(@RequestParam("name") String name,
                                                 @RequestParam("text") String text,
                                                 @RequestParam("knowledgeIds") int[] knowledgeIds,
                                                 @RequestParam("virtualFilePath") String virtualFilePath,
                                                 HttpServletRequest request){
        if (text == null || text.equals("")) {
            if(virtualFilePath!=null){
                //调用ocr
                text = OcrUtil.BaiduOcr(fileSavePath + virtualFilePath);
            }
            else return CommonResult.failed("请输入题目内容或上传图片");
        }

        int userId = (int) request.getAttribute("userId");
        Question question =  questionService.uploadQuestion(name,text,userId,virtualFilePath);
        //上传标签
        boolean flag = questionKnowledgeService.addKnowledgeLabel(question.getId(), knowledgeIds);
        if(flag){
            return CommonResult.success(question, "题目上传成功");
        }
        else {
            return CommonResult.failed("标签上传失败");
        }
    }

    @UserLoginToken
    @ApiOperation(value = "拍照搜题")
    @RequestMapping(value = "/listQuestionByPhoto", method = RequestMethod.GET)
    public CommonPage<Question> listQuestionByPhoto(@RequestParam("virtualFilePath") String virtualFilePath,
                                                    @RequestParam("pageNum") int pageNum,
                                                    @RequestParam("pageSize") int pageSize){
        String text = OcrUtil.BaiduOcr(fileSavePath + virtualFilePath);
        Page<Question> questionPage = questionService.listQuestionByKeyword(text, pageNum, pageSize);
        return CommonPage.restPage(questionPage);
    }

    @UserLoginToken
    @ApiOperation(value = "关键字查询")
    @RequestMapping(value = "/listQuestionByKeyword", method = RequestMethod.GET)
    public CommonPage<Question> listQuestionByKeyword(@RequestParam("keyword") String keyword,
                                                      @RequestParam("pageNum") int pageNum,
                                                      @RequestParam("pageSize") int pageSize){
        Page<Question> questionPage = questionService.listQuestionByKeyword(keyword, pageNum, pageSize);
        return CommonPage.restPage(questionPage);
    }

    @UserLoginToken
    @ApiOperation(value = "添加标签")
    @RequestMapping(value = "/addLabel/{questionId}", method = RequestMethod.POST)
    public CommonResult<String> addLabel(@PathVariable("questionId") int questionId,
                                         @RequestParam("typeIds") int[] typeIds,
                                         @RequestParam("difficultyId") int difficultyId,
                                         HttpServletRequest request){
        int userId = (int) request.getAttribute("userId");
        boolean flag = questionService.addLabels(questionId, userId, typeIds, difficultyId);
        if(flag){
            return CommonResult.success(null,"添加成功");
        }
        else {
            return CommonResult.failed("添加失败");
        }
    }

    @UserLoginToken
    @ApiOperation(value = "根据题目id查询题目")
    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public CommonResult<QAL> list(@PathVariable("id") int id){
        Question questionById = questionService.findQuestionById(id);
        if(questionById == null) return CommonResult.failed("问题不存在");
        int questionId = questionById.getId();
        List<Answer> answers = answerService.listByQuestionId(questionId);
        QAL qal = questionService.findQALById(questionId);
        return CommonResult.success(qal, "返回成功");
    }


}

