package com.xiaozhuo.ctbsb.modules.note.service;

import com.xiaozhuo.ctbsb.modules.note.model.Note;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjq
 * @since 2021-03-15
 */
public interface NoteService extends IService<Note> {
    boolean add(String text, int favoriteId, int questionId);
}
