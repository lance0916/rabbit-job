package com.snail.job.admin.service.impl;

import com.snail.job.admin.model.App;
import com.snail.job.admin.mapper.AppMapper;
import com.snail.job.admin.service.IAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WuQinglong
 * @since 2021-12-06
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

}
