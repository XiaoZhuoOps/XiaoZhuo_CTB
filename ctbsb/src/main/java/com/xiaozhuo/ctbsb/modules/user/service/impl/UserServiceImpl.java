package com.xiaozhuo.ctbsb.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuo.ctbsb.common.exception.Asserts;
import com.xiaozhuo.ctbsb.common.service.RedisService;
import com.xiaozhuo.ctbsb.common.utils.EmailUtil;
import com.xiaozhuo.ctbsb.modules.user.model.User;
import com.xiaozhuo.ctbsb.modules.user.mapper.UserMapper;
import com.xiaozhuo.ctbsb.modules.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaozhuo.ctbsb.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjq
 * @since 2021-02-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final String emailReg = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    @Autowired
    private RedisService redisService;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * generate activate_code, send email
     * @param userParam
     * @return false if username exists or register repeatedly in 1 hour
     * @return true
     */
    @Override
    public String register(User userParam) {

        User user = findUserByName(userParam.getName());
        if(user != null) Asserts.fail("用户名已存在");
        Pattern pattern = Pattern.compile(emailReg);
        Matcher matcher = pattern.matcher(userParam.getEmail());
        if(matcher.find()) {
            //验证邮箱格式是否正确
            String activate_code = UUID.randomUUID().toString().replace("-", "");  //随机数验证码
            //向redis里存入数据和设置缓存时间 hours
            redisService.lPush(userParam.getEmail(), activate_code, 3600);
            redisService.lPush(userParam.getEmail(), userParam, 3600);
            try {
                emailUtil.sendActivateMail(userParam.getEmail(), activate_code);
            } catch (Exception e) {
                Asserts.fail("邮件发送错误");
            }
        }else {
            Asserts.fail("邮箱格式不正确");
        }
        return "发送成功，请1小时内确认";
    }

    /**
     * check the activate_code, add new user
     * @param emailAddress
     * @param activateCode
     * @return commonInfo
     */
    @Override
    public String activate(String emailAddress, String activateCode) {

        if(activateCode == null) Asserts.fail("验证码为空");
        List<Object> userInfo = redisService.lRange(emailAddress, 0, 1);
        if(userInfo == null) Asserts.fail("请先注册");

        String realCode = (String) userInfo.get(0);
        User realUser = (User) userInfo.get(1);
        if(activateCode.equals(realCode)){
            //insert user.status = 1
            User user = findUserByEmail(emailAddress);
            if( user!=null ) Asserts.fail("该邮箱已注册");
            realUser.setStatus(1);
            realUser.setCreatedDate(new Date());
            baseMapper.insert(realUser);
        }
        else {
            Asserts.fail("验证码错误 请重新注册");
        }
        redisService.del(emailAddress);
        return "激活成功 请登录";
    }

    /**
     * verify password and token
     * @return token
     */
    @Override
    public String login(String name, String password) {
        User user = null;
        Pattern pattern = Pattern.compile(emailReg);
        if(pattern.matcher(name).find()) user= findUserByEmail(name);
        else user= findUserByName(name);
        if(user == null) {
            Asserts.fail("用户不存在");
        }
        if(!user.getPassword().equals(password)) {
            Asserts.fail("密码错误");
        }
        if(user.getStatus() != 1){
            Asserts.fail("账户未激活");
        }
        String token = null;
        try{
            token = jwtTokenUtil.getToken(user);
        }
        catch (Exception e){
            Asserts.fail("token生成错误");
        }
        return token;
    }

    @Override
    public User findUserById(int userId) {
        return baseMapper.selectById(userId);
    }

    public User findUserByName(String name){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getName, name);
        return baseMapper.selectOne(queryWrapper);
    }

    public User findUserByEmail(String emailAddress){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, emailAddress);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public int checkCode(String emailAddress) {

        User user = findUserByEmail(emailAddress);
        if(user == null) Asserts.fail("该邮箱不存在");

        Pattern pattern = Pattern.compile(emailReg);
        Matcher matcher = pattern.matcher(emailAddress);
        int checkCode = 0;
        if(matcher.find()){
            checkCode = (int)((Math.random()*9+1)*1000);
            redisService.set(emailAddress, checkCode, 60);
            emailUtil.sendCheckMail(emailAddress, checkCode);
        }else {
            Asserts.fail("邮箱格式错误");
        }
        return checkCode;
    }

    @Override
    public void updatePassword(String emailAddress, int checkCode, String newPassword) {

        User user = findUserByEmail(emailAddress);
        if(user == null) Asserts.fail("该邮箱不存在");

        Object realCode = redisService.get(emailAddress);
        if(realCode == null) Asserts.fail("已过期，请重新发送");
        int code = (int)realCode;
        if(code != checkCode) Asserts.fail("验证码错误");
        else {
            user.setPassword(newPassword);
            updateById(user);
        }
    }

    @Override
    public void updateAvatar(User user, String avatarFilePath) {
        user.setAvatar(avatarFilePath);
        updateById(user);
    }
}
