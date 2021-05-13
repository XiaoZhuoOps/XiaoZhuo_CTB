package com.xiaozhuo.ctbsb.modules.user.controller;


import com.xiaozhuo.ctbsb.common.Api.CommonResult;
import com.xiaozhuo.ctbsb.common.Api.ImageCode;
import com.xiaozhuo.ctbsb.modules.user.model.User;
import com.xiaozhuo.ctbsb.modules.user.service.UserService;
import com.xiaozhuo.ctbsb.security.annotation.UserLoginToken;
import com.xiaozhuo.ctbsb.security.util.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjq
 * @since 2021-02-11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Value("${file-save-path}")
    String fileSavePath;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult<User> register(@RequestBody User userParam) {
        String str = userService.register(userParam);
        return CommonResult.success(null, str);
    }

    @ApiOperation(value = "邮箱激活")
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public CommonResult<User> activate(@RequestParam("emailAddress") String emailAddress,
                                         @RequestParam("activateCode") String activateCode
                                         ){
        String str = userService.activate(emailAddress, activateCode);
        return CommonResult.success(null, str);
    }

    @ApiOperation(value = "获取图片验证码")
    @RequestMapping(value = "/imageCode", method = RequestMethod.GET)
    public void createImageCode(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ImageCode imageCode = createImageCode();
        //todo
        request.getSession().setAttribute("imageCode", imageCode.getCode());
        ImageIO.write(imageCode.getImage(), "jpeg", response.getOutputStream());
    }

    private ImageCode createImageCode() {
        int width = 100; // 验证码图片宽度
        int height = 36; // 验证码图片长度
        int length = 4;  // 验证码位数
        int expireIn = 60; // 验证码有效时间 60s

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = image.getGraphics();

        Random random = new Random();

        graphics.setColor(getRandColor(200, 500));
        graphics.fillRect(0, 0, width, height);
        graphics.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        graphics.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            graphics.drawLine(x, y, x + xl, y + yl);
        }
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            graphics.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            graphics.drawString(rand, 13 * i + 6, 16);
        }
        graphics.dispose();
        return new ImageCode(image, sRand.toString(), expireIn);
    }
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;

        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult<String> login(@RequestParam("name") String name,
                                      @RequestParam("password") String password,
                                      @RequestParam("imageCode") String imageCode,
                                      HttpServletRequest request){
        String code = (String)request.getSession().getAttribute("imageCode");
        if(imageCode.equals("") || !imageCode.equals(code)) return CommonResult.failed("验证码错误");
        String token = userService.login(name, password);
        return CommonResult.success(token,"登录成功");
    }

    @ApiOperation(value = "获取验证码")
    @RequestMapping(value = "/checkCode", method = RequestMethod.GET)

    public CommonResult<Integer> check(@RequestParam("email") String emailAddress){
        int checkCode = userService.checkCode(emailAddress);
        return CommonResult.success(checkCode,"获取成功");
    }

    @UserLoginToken
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)

    public CommonResult<Integer> updatePassword(@RequestParam("email") String emailAddress,
                                       @RequestParam("checkCode") int checkCode,
                                       @RequestParam("newPassword") String newPassword){
        userService.updatePassword(emailAddress, checkCode, newPassword);
        return CommonResult.success(checkCode,"修改成功，请重新登录");
    }

    @UserLoginToken
    @ApiOperation(value = "添加头像")
    @RequestMapping(value = "/addAvatar", method = RequestMethod.POST)
    public CommonResult<String> addAvatar(HttpServletRequest request, @RequestParam("file") MultipartFile uploadFile) {

        int userId = (int) request.getAttribute("userId");

        String absolutelyFile;
        String relativeDist =  "/user" + "/" + userId;
        String fileName = "/" + new Date().getTime() +".jpg";

        User user = userService.findUserById(userId);
        if(user == null) return CommonResult.failed("用户不存在");

        File absolutelyDist = new File(fileSavePath + relativeDist);
        try{
            if(!absolutelyDist.isDirectory()){
                boolean mkdir = absolutelyDist.mkdirs();
            }
            absolutelyFile = absolutelyDist + fileName;
            File avatarFile = new File(absolutelyFile);
            uploadFile.transferTo(avatarFile);
        }catch (Exception e){
            return CommonResult.failed("文件上传失败");
        }
        String virtualFile = relativeDist + fileName;
        userService.updateAvatar(user, virtualFile);
        return CommonResult.success(virtualFile,"头像修改成功");
    }
}

