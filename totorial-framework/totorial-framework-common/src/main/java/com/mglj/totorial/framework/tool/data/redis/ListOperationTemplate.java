package com.mglj.totorial.framework.tool.data.redis;

import com.mglj.totorial.framework.tool.converter.StringConverter;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;


/**
 * Created by zsp on 2018/8/9.
 */
public class ListOperationTemplate<K, V> extends AbstractOperationTemplate<K, V> {
    private final ListOperations<String, String> listOps;


    protected ListOperationTemplate(Class<K> keyType,
                                    Class<V> valueType,
                                    StringRedisTemplate stringRedisTemplate,
                                    String namespace,
                                    String domain,
                                    Operations operations) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations);
        this.listOps = stringRedisTemplate.opsForList();
    }

    protected ListOperationTemplate(Class<K> keyType,
                                    Class<V> valueType,
                                    StringRedisTemplate stringRedisTemplate,
                                    String namespace,
                                    String domain,
                                    Operations operations,
                                    StringConverter<V> converter) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations, converter);
        this.listOps = stringRedisTemplate.opsForList();
    }


    /**
     * 返回存储在 key 的列表里指定范围内的元素。
     * start 和 end 偏移量都是基于0的下标，即list的第一个元素下标是0（list的表头），第二个元素下标是1，以此类推。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<V> range(K key, long start, long end){
               validateKey(key);
        String key0 = wrapKey(key);
        List<String> stringValueList = operations.execute(LRANGE, getKeyPrefix(), Arrays.asList(key0), () -> {
            return listOps.range(key0,start,end);
        },start,end);
        List<V> valueList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(stringValueList)) {
            for(String stringValue : stringValueList) {
                valueList.add(converter.deserialize(stringValue, valueType));
            }
        }
        return valueList;
    }

    /**
     * 返回存储在 key 里的list的长度。
     * 如果 key 不存在，那么就被看作是空list，并且返回长度为 0。
     * 当存储在 key 里的值不是一个list的话，会返回error。
     * @param key
     * @return
     */
    public Long size(K key){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(LLEN, getKeyPrefix(), Arrays.asList(key0), () -> {
            return listOps.size(key0);
        });
    }

    /**
     * 移除并且返回 key 对应的 list 的左侧第一个元素。
     * @param key
     * @return
     */
    public V leftPop(K key){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(LPOP, getKeyPrefix(), Arrays.asList(key0), () -> {
              return converter.deserialize(listOps.leftPop(key0), valueType);
        });
    }
    /**
     * * 移除并且返回 key 对应的 list 的右侧第一个元素。
     * @param key
     * @return
     */
    public V rightPop(K key){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(RPOP, getKeyPrefix(), Arrays.asList(key0), () -> {
            return converter.deserialize(listOps.rightPop(key0), valueType);
        });

    }

    /**将所有指定的值插入到存于 key 的列表的头部。
     * 如果 key 不存在，那么在进行 push 操作前会创建一个空列表。
     * 如果 key 对应的值不是一个 list 的话，那么会返回一个错误。
     * @param key
     * @param value
     * @return
     */
    public Long leftPush(K key, V value){
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(LPUSH, getKeyPrefix(), Arrays.asList(key0), () -> {
            return listOps.leftPush(key0,value0);
        },value0);
    }


    public Long leftPushAll(K key, V... values){
        validateKey(key);
        String key0 = wrapKey(key);
        int length = values.length;
        String[] valueArray = new String[length];
        for(int i = 0; i < length; i++) {
            valueArray[i] = converter.serialize(values[i]);
        }
        return operations.execute(LPUSH, getKeyPrefix(), Arrays.asList(key0), () -> {
            return listOps.leftPushAll(key0, valueArray);
        }, valueArray);

    }

    /**
     * 只有当 key 已经存在并且存着一个 list 的时候，
     * 在这个 key 下面的 list 的头部插入 value。
     * 与 LPUSH 相反，当 key 不存在的时候不会进行任何操作。
     * @param key
     * @param value
     * @return
     */
    public Long leftPushIfPresent(K key, V value){
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(LPUSHX, getKeyPrefix(), Arrays.asList(key0), () -> {
            return listOps.leftPushIfPresent(key0,value0);
        },value0);
    }



    /**
     * 向存于 key 的列表的尾部插入所有指定的值。
     * 如果 key 不存在，那么会创建一个空的列表然后再进行 push 操作。
     * 当 key 保存的不是一个列表，那么会返回一个错误。
     * @param key
     * @param value
     * @return
     */
    public Long rightPush(K key, V value){
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(RPUSH, getKeyPrefix(), Arrays.asList(key0), () -> {
            return listOps.rightPush(key0,value0);
        },value0);
    }


    public  Long rightPushAll(K key, V... values){
        validateKey(key);
        String key0 = wrapKey(key);
        int length = values.length;
        String[] valueArray = new String[length];
        for(int i = 0; i < length; i++) {
            valueArray[i] = converter.serialize(values[i]);
        }
        return operations.execute(RPUSH, getKeyPrefix(), Arrays.asList(key0), () -> {
            return listOps.rightPushAll(key0, valueArray);
        }, valueArray);

    }

    /**
     * 将值 value 插入到列表 key 的表尾, 当且仅当 key 存在并且是一个列表。
     * 和 RPUSH 命令相反, 当 key 不存在时，RPUSHX 命令什么也不做。
     * @param key
     * @param value
     * @return
     */
    public  Long rightPushIfPresent(K key, V value){
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(RPUSHX, getKeyPrefix(), Arrays.asList(key0), () -> {
            return listOps.rightPushIfPresent(key0,value0);
        },value0);
    }



    /**
     * 返回列表里的元素的索引 index 存储在 key 里面。
     * 下标是从0开始索引的，所以 0 是表示第一个元素， 1 表示第二个元素，并以此类推。
     * 负数索引用于指定从列表尾部开始索引的元素。
     * 在这种方法下，-1 表示最后一个元素，-2 表示倒数第二个元素，并以此往前推。
     * @param key
     * @param index
     * @return
     */
    public V index(K key, long index){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(LINDEX, getKeyPrefix(), Arrays.asList(key0), () -> {
            return converter.deserialize(listOps.index(key0,index), valueType);
        },index);
    }


    /**
     * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作：
     count > 0: 从头往尾移除值为 value 的元素。
     count < 0: 从尾往头移除值为 value 的元素。
     count = 0: 移除所有值为 value 的元素。
     比如， LREM list -2 “hello” 会从存于 list 的列表里移除最后两个出现的 “hello”。
     需要注意的是，如果list里没有存在key就会被当作空list处理，所以当 key 不存在的时候，这个命令会返回 0。
     * @param key
     * @param count
     * @param value
     * @return  被移除的元素个数。
     */
    public Long remove(K key, long count, V value){
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(LREM, getKeyPrefix(), Arrays.asList(key0), () -> {
            return listOps.remove(key0,count,value0);
        },count,value0);

    }





}
