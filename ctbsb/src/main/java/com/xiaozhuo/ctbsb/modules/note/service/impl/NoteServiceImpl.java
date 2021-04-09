package com.xiaozhuo.ctbsb.modules.note.service.impl;

import com.xiaozhuo.ctbsb.modules.note.model.Note;
import com.xiaozhuo.ctbsb.modules.note.mapper.NoteMapper;
import com.xiaozhuo.ctbsb.modules.note.service.NoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjq
 * @since 2021-03-15
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {
    @Override
    public boolean add(String text, int favoriteId, int questionId) {
        Note note = new Note();
        note.setText(text);
        note.setFavoriteId(favoriteId);
        note.setQuestionId(questionId);
        int insert = getBaseMapper().insert(note);
        return 0<insert;
    }
}
