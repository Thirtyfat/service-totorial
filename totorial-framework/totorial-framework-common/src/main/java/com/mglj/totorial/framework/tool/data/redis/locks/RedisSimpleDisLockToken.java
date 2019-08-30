package com.mglj.totorial.framework.tool.data.redis.locks;


import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLockToken;

/**
 * Created by zsp on 2018/9/19.
 */
public class RedisSimpleDisLockToken implements SimpleDisLockToken {

    private String namespace;
    private String domain;

    public RedisSimpleDisLockToken(String namespace, String domain) {
        this.namespace = namespace;
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedisSimpleDisLockToken that = (RedisSimpleDisLockToken) o;

        if (namespace != null ? !namespace.equals(that.namespace) : that.namespace != null) return false;
        return domain != null ? domain.equals(that.domain) : that.domain == null;
    }

    @Override
    public int hashCode() {
        int result = namespace != null ? namespace.hashCode() : 0;
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getDomain() {
        return domain;
    }
}
