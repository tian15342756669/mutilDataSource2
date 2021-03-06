package com.hispeed.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 切面类，根据自定义数据源注解来进行切面操作--切换数据源
 * Created by dengtg on 2018-8-28.
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {

    private static final Logger logger = LogManager.getLogger(DynamicDataSourceAspect.class);

    /**
     * 切换数据库
     * @param point
     * @param dataSourceAnnotation
     * @return
     * @throws Throwable
     */
    @Before("@annotation(dataSourceAnnotation)")
    public void changeDataSource(JoinPoint point, DataSourceAnnotation dataSourceAnnotation){
        Object[] methodArgs = point.getArgs();
        String dsId = methodArgs[methodArgs.length-1].toString();

        if(!DynamicDataSourceContextHolder.existDateSoure(dsId)){
            logger.error("No data source found ...【"+dsId+"】");
            return;
        }else{
            System.out.println(dsId);
            DynamicDataSourceContextHolder.setDateSoureType(dsId);
        }
    }

    /**
     * @Title: destroyDataSource
     * @Description: 销毁数据源  在所有的方法执行执行完毕后
     * @param point
     * @param dataSourceAnnotation
     * @return void
     * @throws
     */
    @After("@annotation(dataSourceAnnotation)")
    public void destroyDataSource(JoinPoint point,DataSourceAnnotation dataSourceAnnotation){
        DynamicDataSourceContextHolder.clearDateSoureType();
    }
}
