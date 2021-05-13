package com.xiaozhuo.ctbsb.modules.note.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuo.ctbsb.common.exception.ApiException;
import com.xiaozhuo.ctbsb.common.exception.Asserts;
import com.xiaozhuo.ctbsb.modules.note.model.Note;
import com.xiaozhuo.ctbsb.modules.note.mapper.NoteMapper;
import com.xiaozhuo.ctbsb.modules.note.service.NoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjq
 * @since 2021-03-15
 */
@Service
@Transactional(rollbackFor = ApiException.class)
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {
    @Override
    public boolean add(String text, int favoriteId, int questionId) {
        Note note = find(favoriteId, questionId);
        if(null != note){
            note.setText(text);
            getBaseMapper().updateById(note);
        }
        else{
            Note newNote = new Note();
            newNote.setText(text);
            newNote.setFavoriteId(favoriteId);
            newNote.setQuestionId(questionId);
            try{
                getBaseMapper().insert(newNote);
            }catch (Exception e){
                Asserts.fail("插入失败");
            }
        }
        return true;
    }

    @Override
    public Note find(int favoriteId, int questionId) {
        return getBaseMapper().selectOne(new QueryWrapper<Note>().lambda().eq(Note::getFavoriteId, favoriteId)
                .eq(Note::getQuestionId, questionId));
    }
}
