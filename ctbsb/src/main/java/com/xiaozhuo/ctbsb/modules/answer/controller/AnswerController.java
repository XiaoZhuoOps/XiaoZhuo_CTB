package com.xiaozhuo.ctbsb.modules.answer.controller;


import com.xiaozhuo.ctbsb.common.Api.CommonResult;
import com.xiaozhuo.ctbsb.common.utils.OcrUtil;
import com.xiaozhuo.ctbsb.modules.answer.model.Answer;
import com.xiaozhuo.ctbsb.modules.answer.service.AnswerService;
import com.xiaozhuo.ctbsb.modules.question.model.Question;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjq
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    AnswerService answerService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Value("${file-save-path}")
    String fileSavePath;

    @UserLoginToken
    @ApiOperation(value = "上传图片")
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public CommonResult<String> uploadImg(@RequestParam("virtualFilePath") MultipartFile uploadFile,
                                          HttpServletRequest request){

        int userId = (int) request.getAttribute("userId");
        String trueFilePath = null;
        String relativeDistPath =  "/answer/"+ userId;
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
    @ApiOperation(value = "上传答案")
    @RequestMapping(value = "/uploadAnswer/{questionId}", method = RequestMethod.POST)
    public CommonResult<Answer> uploadQuestion(@PathVariable("questionId") int questionId,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("text") String text,
                                                 @RequestParam("virtualFilePath") String virtualFilePath,
                                                 HttpServletRequest request){
        if (text == null || text.equals("")) {
            if(virtualFilePath!=null){
                //调用ocr
                text = OcrUtil.BaiduOcr(fileSavePath + virtualFilePath);
            }
            else return CommonResult.failed("请输入答案内容或上传图片");
        }

        int userId = (int) request.getAttribute("userId");
        Answer answer = answerService.uploadAnswer(name, text, virtualFilePath, userId, questionId);
        if(answer!=null){
            return CommonResult.success(answer, "答案上传成功");
        }
        else {
            return CommonResult.failed("答案上传失败");
        }
    }
}

