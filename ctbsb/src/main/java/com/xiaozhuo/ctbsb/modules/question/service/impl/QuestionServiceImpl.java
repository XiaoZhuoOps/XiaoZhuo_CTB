package com.xiaozhuo.ctbsb.modules.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaozhuo.ctbsb.common.exception.ApiException;
import com.xiaozhuo.ctbsb.common.exception.Asserts;

import com.xiaozhuo.ctbsb.domain.QAL;
import com.xiaozhuo.ctbsb.modules.answer.mapper.AnswerMapper;
import com.xiaozhuo.ctbsb.modules.answer.model.Answer;
import com.xiaozhuo.ctbsb.modules.favorite.mapper.FavoriteMapper;
import com.xiaozhuo.ctbsb.modules.favorite.mapper.FavoriteQuestionMapper;
import com.xiaozhuo.ctbsb.modules.favorite.model.Favorite;
import com.xiaozhuo.ctbsb.modules.favorite.model.FavoriteQuestion;
import com.xiaozhuo.ctbsb.modules.note.mapper.NoteMapper;
import com.xiaozhuo.ctbsb.modules.note.model.Note;
import com.xiaozhuo.ctbsb.modules.question.mapper.*;
import com.xiaozhuo.ctbsb.modules.question.model.*;
import com.xiaozhuo.ctbsb.modules.question.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjq
 * @since 2021-02-16
 */
@Service
@Transactional(rollbackFor = ApiException.class)
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    QuestionTypeMapper questionTypeMapper;

    @Autowired
    QuestionDifficultyMapper questionDifficultyMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    AnswerMapper answerMapper;

    @Autowired
    KnowledgeMapper knowledgeMapper;

    @Autowired
    TypeMapper typeMapper;

    @Autowired
    DifficultyMapper difficultyMapper;

    @Autowired
    FavoriteQuestionMapper favoriteQuestionMapper;

    @Autowired
    NoteMapper noteMapper;

    @Autowired
    FavoriteMapper favoriteMapper;

    @Override
    public Question findQuestionByName(String name) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Question::getName, name);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Question uploadQuestion(String name, String text, int uploadId, String questionImagePath) {
        Question questionParam = new Question();
        questionParam.setName(name);
        questionParam.setText(text);
        questionParam.setUpId(uploadId);
        questionParam.setImage(questionImagePath);
        questionParam.setCreatedDate(new Date());
        try{
            baseMapper.insert(questionParam);
        }catch (Exception e){
            Asserts.fail("题目上传失败");
        }
        return questionParam;
    }

    @Override
    public Page<Question> listQuestionByKeyword(String keyword, int pageNum, int pageSize) {
        Page<Question> questionPage = new Page<>(pageNum, pageSize);
        return questionMapper.selectByKw(questionPage, keyword);
    }

    @Override
    public Question findQuestionById(int id) {
        return getById(id);
    }

    @Override
    public Page<Question> listQuestionByIds(List<Integer> ids, int pageNum, int pageSize) {
        Page<Question> questionPage = new Page<>(pageNum, pageSize);
        return page(questionPage, new QueryWrapper<Question>().lambda().in(Question::getId,ids));
    }

    @Override
    public boolean addLabels(int questionId, int userId, int[] typeIds, int difficultyId) {
        try{
            deleteLabels(questionId, userId);
            for (int typeId : typeIds) {
                QuestionType questionType = new QuestionType();
                questionType.setUserId(userId);
                questionType.setTypeId(typeId);
                questionType.setQuestionId(questionId);
                questionTypeMapper.insert(questionType);
            }
            QuestionDifficulty questionDifficulty = new QuestionDifficulty();
            questionDifficulty.setUserId(userId);
            questionDifficulty.setDifficultyId(difficultyId);
            questionDifficulty.setQuestionId(questionId);
            questionDifficultyMapper.insert(questionDifficulty);
        }catch (Exception e){
            Asserts.fail("添加失败");
        }
        return true;
    }

    @Override
    public boolean deleteLabels(int questionId, int userId) {
        try{
            int delete1 = questionTypeMapper.delete(new QueryWrapper<QuestionType>().lambda().eq(QuestionType::getQuestionId, questionId)
                .eq(QuestionType::getUserId, userId));
            int delete2 = questionDifficultyMapper.delete(new QueryWrapper<QuestionDifficulty>().lambda().eq(QuestionDifficulty::getQuestionId, questionId)
                    .eq(QuestionDifficulty::getUserId, userId));
        }catch (Exception e){
            Asserts.fail("删除失败");
        }
        return true;
    }
    //AQL
    @Override
    public QAL findQALById(int questionId, int userId) {
        Question question = getBaseMapper().selectById(questionId);
        List<Answer> answers = answerMapper.selectList(new QueryWrapper<Answer>().lambda().eq(Answer::getQuestionId, questionId));
        List<Knowledge> knowledges = knowledgeMapper.selectByQuestionId(questionId);
        List<Difficulty> difficulties = difficultyMapper.selectByQuestionId(questionId, userId);
        Difficulty difficulty = (difficulties.size()!=0)? difficulties.get(0):null;
        List<Type> types = typeMapper.selectByQuestionId(questionId, userId);
        return new QAL(question, answers, difficulty, knowledges, types);
    }

    @Override
    public Page<QAL> listQALByIds(List<Integer> ids, int userId, int pageNum, int pageSize) {
        ArrayList<QAL> qals = new ArrayList<>(ids.size());
        for(Integer id:ids){
            qals.add(findQALById(id, userId));
        }
        //list -> page
        Page<QAL> qalPage = new Page<>(pageNum, pageSize);
        qalPage.setRecords(qals);
        return qalPage;
    }

    @Override
    public boolean addQuestionToFavorite(int userId, int questionId, int[] favoriteIds, int[] typeIds, int difficultyId, String text) {
        try{
            //删除原有的favoriteQuestion
            List<Favorite> favorites = favoriteMapper.selectFavoriteByQuestion(userId, questionId);
            for(Favorite f:favorites){
                favoriteQuestionMapper.delete(new QueryWrapper<FavoriteQuestion>().lambda().eq(FavoriteQuestion::getQuestionId, questionId)
                        .eq(FavoriteQuestion::getFavoriteId, f.getId()));
            }
            //调用类内部的其他方法会执行事务吗？
            addLabels(questionId, userId, typeIds, difficultyId);
            for(int favoriteId:favoriteIds) {
                FavoriteQuestion favoriteQuestion = new FavoriteQuestion();
                favoriteQuestion.setFavoriteId(favoriteId);
                favoriteQuestion.setQuestionId(questionId);
                favoriteQuestionMapper.insert(favoriteQuestion);
                Note note = new Note();
                note.setText(text);
                note.setFavoriteId(favoriteId);
                note.setQuestionId(questionId);
                noteMapper.insert(note);
            }
        }catch (Exception e){
            Asserts.fail("添加失败");
        }
        return true;
    }

    @Override
    public List<Question> listQuestion() {
        return getBaseMapper().selectAll();
    }
}
