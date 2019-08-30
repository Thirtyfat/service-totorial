package com.mglj.totorial.framework.tool.data.redis;

/**
 * Created by zsp on 2018/8/23.
 */
public enum RedisOperationEnum {

    TYPE,
    //string
    DEL,
    EXISTS,
    EXPIRE,
    EXPIREAT,
    GET,
    GETBIT,
    GETSET,
    INCRBY,
    INCRBYFLOAT,
    MGET,
    MSET,
    SCAN,
    SET,
    SETBIT,
    SETNX,
    TTL,
    //hash
    HDEL,
    HEXISTS,
    HGET,
    HGETALL,
    HINCRBY,
    HINCRBYFLOAT,
    HKEYS,
    HLEN,
    HMGET,
    HMSET,
    HSCAN,
    HSET,
    HSETNX,
    HVALS,
    //set
    SADD,
    SCARD,
    SISMEMBER,
    SMEMBERS,
    SREM,
    SSCAN,
    //list
    LRANGE,
    LLEN,
    LPOP,
    LPUSH,
    LPUSHX,
    RPOP,
    RPUSH,
    RPUSHX,
    LINDEX,
    LINSERT,
    LREM,

    //zset
    ZADD,
    ZREM,
    ZRANK,
    ZREVRANK,
    ZSCAN,
    ZRANGE,
    ZRANGEBYSCORE,
    ZREVRANGE,
    ZCOUNT,
    ZCARD,
    ZSCORE,
    ZREMRANGEBYRANK,
    ZREMRANGEBYSCORE



}
