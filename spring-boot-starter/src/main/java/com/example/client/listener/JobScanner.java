package com.example.client.listener;

import com.example.client.annotation.RabbitJob;
import com.example.client.handler.impl.MethodJobHandler;
import com.example.common.exception.RegisterJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

import static com.example.client.constant.JobConstants.handlerRepository;

/**
 * 扫描任务并注册
 * @author WuQinglong
 */
@Component
public class JobScanner implements ApplicationContextAware {
    private final Logger log = LoggerFactory.getLogger(JobScanner.class);

    /**
     * Spring 上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 扫描项目中的任务并进行注册
     */
    public void scanAndRegister() {
        if (applicationContext == null) {
            log.error("没有Spring应用的上下文，无法扫描JobHandler！");
            return;
        }

        // 获取 Spring 中的所有 bean
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class, false, true);

        // 扫描 bean 中含有 Job 注解的方法
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);

            // 获取类中的方法注解集合
            Map<Method, RabbitJob> methodJobMap = MethodIntrospector.selectMethods(
                    bean.getClass(),
                    new MethodIntrospector.MetadataLookup<RabbitJob>() {
                        @Override
                        public RabbitJob inspect(@NonNull Method method) {
                            return AnnotatedElementUtils.findMergedAnnotation(method, RabbitJob.class);
                        }
                    }
            );
            if (methodJobMap.isEmpty()) {
                continue;
            }

            // 注册类中的任务
            for (Map.Entry<Method, RabbitJob> entry : methodJobMap.entrySet()) {
                Method method = entry.getKey();
                RabbitJob jobInfo = entry.getValue();

                // 校验任务名
                String jobName = jobInfo.name();
                if (jobName.trim().length() == 0) {
                    throw new RegisterJobException("无效的任务名。for[" + bean.getClass() + "#" + method.getName() + "]");
                }

                // 校验任务是否重复
                if (handlerRepository.get(jobName) != null) {
                    throw new RegisterJobException("任务名存在冲突, for[" + jobName + "]");
                }

                // 初始化方法
                Method initMethod = setMethodAccessible(bean, jobInfo.init());
                // 销毁方法
                Method destroyMethod = setMethodAccessible(bean, jobInfo.destroy());

                // 检验方法的参数. 1:只有一个参数; 2:参数必须是String类型的; 3:返回值必须是 ResultT 类型
//                if (method.getParameterTypes().length > 0) {
//                    throw new RegisterJobException("JobHandler的方法入参无效, " +
//                            "for[" + bean.getClass() + "#" + method.getName() + "], " +
//                            "格式如:\" public void execute() \".");
//                }
//                if (!method.getReturnType().isAssignableFrom(Void.class)) {
//                    throw new RegisterJobException("JobHandler的方法返回值无效, " +
//                            "for[" + bean.getClass() + "#" + method.getName() + "], " +
//                            "格式如:\" public void execute() \".");
//                }
                method.setAccessible(true);

                // 注册 JobHandler
                log.info("注册任务:{}", jobName);
                handlerRepository.put(jobName, new MethodJobHandler(bean, method, initMethod, destroyMethod));
            }
        }
    }

    /**
     * 设置方法可访问
     */
    private Method setMethodAccessible(Object bean, String methodName) {
        if (methodName != null && methodName.length() > 0) {
            try {
                Method method = bean.getClass().getDeclaredMethod(methodName);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
                throw new RegisterJobException("初始化或销毁方法无效，for[" + bean.getClass() + "#" + methodName + "]");
            }
        }
        return null;
    }

}
