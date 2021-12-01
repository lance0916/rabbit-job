package com.snail.job.admin.service;

import com.snail.job.admin.controller.vo.JobLogVO;
import com.snail.job.admin.repository.JobLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 吴庆龙
 * @date 2020/7/27 10:37 上午
 */
@Service
public class LogService {

    @Resource
    private JobLogRepository jobLogRepository;

    /**
     * 分页查询
     */
    public Page<JobLogVO> list(String appName, Integer jobId, Integer triggerCode, Integer execCode,
                               Date triggerBeginDate, Date triggerEndDate, Integer pageNum, Integer pageSize) {
//        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
//        return jobLogRepository.findAll((Specification<JobLogVO>) (root, query, builder) -> {
//            Predicate predicate = builder.conjunction();
//            List<Expression<Boolean>> expressions = predicate.getExpressions();
//
//            return predicate;
//        }, pageRequest);
        return null;
    }

}
