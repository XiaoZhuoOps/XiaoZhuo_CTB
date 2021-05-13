package com.xiaozhuo.ctbsb.modules.question.service.impl;
import com.sun.org.apache.bcel.internal.generic.LUSHR;
import com.xiaozhuo.ctbsb.common.utils.LatexUtil;
import com.xiaozhuo.ctbsb.modules.question.mapper.QuestionRepository;
import com.xiaozhuo.ctbsb.modules.question.model.Question;
import com.xiaozhuo.ctbsb.modules.question.service.ElasticsearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaozhuo
 * @date 2021/4/20 15:51
 */

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    static private Log log = LogFactory.getLog(ElasticsearchServiceImpl.class);

    @Autowired
    private ElasticsearchTemplate elasticTemplate;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void saveQuestion(Question question) {
        //latex预处理
        question.setPlainText(LatexUtil.latexHandle(question.getText()));
        questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(int id) {
        questionRepository.deleteById(id);
    }

    @Override
    public Page<Question> searchQuestion(String kw, int pageNum, int pageSize) {
        //kw预处理
        kw = LatexUtil.latexHandle(kw);
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(kw,"name", "plainText"))
                .withSort(SortBuilders.fieldSort("createdDate").order(SortOrder.DESC))
                .withHighlightFields(
                        new HighlightBuilder.Field("name").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("plainText").preTags("<em>").postTags("</em>")
                )
                .build();

        return elasticTemplate.queryForPage(query, Question.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHits hits = searchResponse.getHits();
                if (hits.getTotalHits() <= 0) {
                    return null;
                }
                List<Question> questions = new ArrayList<>();
                for(SearchHit hit : hits){
                    Question question = new Question();

                    String id = hit.getSourceAsMap().get("id").toString();
                    question.setId(Integer.valueOf(id));

                    String name = hit.getSourceAsMap().get("name").toString();
                    question.setName(name);

                    String image = hit.getSourceAsMap().get("image").toString();
                    question.setImage(image);

                    String text = hit.getSourceAsMap().get("text").toString();
                    question.setText(text);

                    String plainText = hit.getSourceAsMap().get("plainText").toString();
                    question.setPlainText(plainText);

                    String upId = hit.getSourceAsMap().get("upId").toString();
                    question.setUpId(Integer.valueOf(upId));

                    String createdDate = hit.getSourceAsMap().get("createdDate").toString();
                    question.setCreatedDate(new Date(Long.parseLong(createdDate)));

                    questions.add(question);
                }

                return new AggregatedPageImpl(questions, pageable,
                        hits.getTotalHits(), searchResponse.getAggregations(), searchResponse.getScrollId(), hits.getMaxScore());
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }
        });
    }
}
