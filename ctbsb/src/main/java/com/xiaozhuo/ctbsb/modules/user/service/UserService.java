package com.xiaozhuo.ctbsb.modules.user.service;

import com.xiaozhuo.ctbsb.modules.user.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjq
 * @since 2021-02-11
 */
public interface UserService extends IService<User> {

    String register(User userParam);
    String activate(String emailAddress, String checkCode);
    String login(String name, String password);
    User findUserById(int userId);
    User findUserByName(String name);
    int checkCode(String emailAddress);
    void updatePassword(String emailAddress, int checkCode, String newPassword);
    void updateAvatar(User userParam, String avatarFilePath);
}
