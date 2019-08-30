package com.mglj.totorial.framework.tool.data.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * Created by zsp on 2018/9/3.
 */
@Intercepts({@Signature(type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DialectInterceptor implements Interceptor {

    /**
     * master sql hint
     */
    private final static String MASTER_SQL_HINT = "/*FORCE_MASTER*/";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //当前环境 MappedStatement，BoundSql，及sql取得
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String origSql = boundSql.getSql().trim();

        if (needToAddMasterSqlHint(origSql)) {
            BoundSql dbRouterBoundSql = copyFromBoundSql(mappedStatement, boundSql, dbRouterSql(origSql));
            MappedStatement dbRouterMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(dbRouterBoundSql));
            invocation.getArgs()[0] = dbRouterMs;
        }

        return invocation.proceed();
    }

    /**
     * 复制MappedStatement对象
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (null != ms.getKeyProperties()) {
            if (ms.getKeyProperties().length > 0) {
                builder.keyProperty(ms.getKeyProperties()[0]);
            }
        }

        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    /**
     * 复制BoundSql对象
     */
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    private boolean needToAddMasterSqlHint(String sql) {
//        Role role = DynamicDataSourceContext.getRole();
//
//        if (role != null && Role.WRITABLE.equals(role)
//                && !StringUtils.isEmpty(masterSqlHint)
//                && !StringUtils.contains(sql, masterSqlHint)) {
//            return true;
//        }
        return false;
    }

    private String dbRouterSql(String sql) {
        String dbRouterSql = sql;
        if (needToAddMasterSqlHint(sql)) {
            dbRouterSql = MASTER_SQL_HINT + " " + sql;
        }
        return dbRouterSql;
    }

    /**
     * 拦截器对应的封装原始对象的方法
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 设置注册拦截器时设定的属性
     */
    @Override
    public void setProperties(Properties p) {

    }

    private class BoundSqlSqlSource implements SqlSource {

        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
