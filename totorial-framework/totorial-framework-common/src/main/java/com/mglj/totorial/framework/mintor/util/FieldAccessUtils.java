package com.mglj.totorial.framework.mintor.util;

import com.google.common.collect.Sets;
import com.mglj.totorial.framework.mintor.DataSourceException;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 值属性访问，一般比如对象某个字段xxx，必须有相应的getXxx()公共方法，才能正确访问。
 *
 * Created by zsp on 2019/3/27.
 */
public class FieldAccessUtils {

    private final static LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer
            = new LocalVariableTableParameterNameDiscoverer();

    private FieldAccessUtils() {

    }

    /**
     * 获取属性值
     *
     * @param arguments             方法入参集合
     * @param parameterNames        方法入参参数名集合
     * @param fieldSet              要返回值的字段名集合
     * @return
     */
    public static Map<String, Object> getAllFieldValue(Object[] arguments,
                                                       String[] parameterNames,
                                                       Set<String> fieldSet) {
        Objects.requireNonNull(arguments, "The arguments is required.");
        Objects.requireNonNull(parameterNames, "The parameterNames is required.");
        Objects.requireNonNull(fieldSet, "The fieldSet is required.");

        Map<String, Object> fieldValueMap = new HashMap<>();
        Object argument;
        String parameterName;
        boolean isMemberOfCollection;
        for(int i = 0, len = arguments.length; i < len; i++) {
            argument = arguments[i];
            if(argument == null) {
                continue;
            }
            parameterName = parameterNames[i];
            isMemberOfCollection = false;
            for(;;) {
                if (((argument instanceof Number)
                        || (argument instanceof Boolean)
                        || (argument instanceof Character)
                        || (argument instanceof String))
                        && (fieldSet.contains(parameterName) || isMemberOfCollection)) {
                    fieldValueMap.put(parameterName, argument);
                } else if (argument.getClass().isArray()) {
                    Object[] array = (Object[]) argument;
                    if (array.length > 0) {
                        argument = array[0];
                        isMemberOfCollection = fieldSet.contains(parameterName);
                        continue;
                    }
                } else if (Collection.class.isAssignableFrom(argument.getClass())) {
                    Collection collection = (Collection) argument;
                    Iterator it = collection.iterator();
                    if (it.hasNext()) {
                        argument = it.next();
                        isMemberOfCollection = fieldSet.contains(parameterName);
                        continue;
                    }
                } else if (Map.class.isAssignableFrom(argument.getClass())) {
                    Map map = (Map) argument;
                    Object fieldValue;
                    for (String field : fieldSet) {
                        fieldValue = map.get(field);
                        if (fieldValue != null) {
                            fieldValueMap.put(field, fieldValue);
                        }
                    }
                } else {
                    Object fieldValue;
                    for (String field : fieldSet) {
                        fieldValue = FieldAccessUtils.getFieldValue(argument, field);
                        if (fieldValue != null) {
                            fieldValueMap.put(field, fieldValue);
                        }
                    }
                }
                break;
            }
        }

        return fieldValueMap;
    }

    /**
     * 获取属性值
     *
     * @param arguments             方法入参集合
     * @param parameterNames        方法入参参数名集合
     * @param field                 要返回值的字段名
     * @return
     */
    public static Object getFieldValue(Object[] arguments,
                                       String[] parameterNames,
                                       String field) {
       Map<String, Object> map = getAllFieldValue(arguments, parameterNames, Sets.newHashSet(field));
       Iterator<String> it = map.keySet().iterator();
       if(it.hasNext()) {
           return map.get(it.next());
       }

       return null;
    }

    /**
     * 返回方法的入参参数名集合
     *
     * @param method                方法
     * @return
     */
    public static String[] getParameterNames(Method method) {
        Objects.requireNonNull(method, "The method is required.");
        return localVariableTableParameterNameDiscoverer.getParameterNames(method);

        //另一种获取方式：
        //List<String> paramterList = new ArrayList<>();
        //Parameter[] params = method.getParameters();
        //for(Parameter parameter : params){
        //    paramterList.add(parameter.getName());
        //}
        //如果编译级别低于1.8，得到的参数名称是无意义的arg0、arg1……
        //保留参数名这一选项由编译开关javac -parameters打开，默认是关闭的。
        //注意此功能必须把代码编译成1.8版本的class才行。
        //idea设置保留参数名：
        //Java Compiler->设置模块字节码版本1.8，Javac Options中的 Additional command line parameters: -parameters
    }

    private static Object getFieldValue(Object argument, String field) {
        String methodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
        try {
            Method method = ReflectionUtils.findMethod(argument.getClass(), methodName);
            if(method != null) {
                return method.invoke(argument);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DataSourceException(e);
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b");
        System.out.println(Collection.class.isAssignableFrom(list.getClass()));
        Set<String> set = Sets.newHashSet("a", "b");
        System.out.println(Collection.class.isAssignableFrom(set.getClass()));
        String[] array = new String[]{"a", "b"};
        System.out.println(array.getClass().isArray());
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Tom");
        map.put("count", 200);
        System.out.println(Map.class.isAssignableFrom(map.getClass()));

        Method methodA = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodA", List.class);
        System.out.println(getFieldValue(new Object[] {list}, getParameterNames(methodA), "list"));

        Method methodB = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodB", Set.class);
        System.out.println(getFieldValue(new Object[] {set}, getParameterNames(methodB), "set"));

        Method methodC = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodC", String[].class);
        System.out.println(getFieldValue(new Object[] {array}, getParameterNames(methodC), "array"));

        Method methodD = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodD", String.class);
        System.out.println(getFieldValue(new Object[] {"hello"}, getParameterNames(methodD), "value"));

        Method methodE = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodE", String.class, Long.class);
        System.out.println(getFieldValue(new Object[] {"world", 10}, getParameterNames(methodE), "value2"));
        System.out.println(getAllFieldValue(new Object[] {"world", 10}, getParameterNames(methodE),
                Sets.newHashSet("value1", "value2")));

        Method methodF = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodF", String.class, Tester.class);
        System.out.println(getFieldValue(new Object[] {"Good", new Tester(2L, "Bad")}, getParameterNames(methodF), "name"));
        System.out.println(getAllFieldValue(new Object[] {"Good", new Tester(2L, "Bad")}, getParameterNames(methodF),
                Sets.newHashSet("id", "name")));
        System.out.println(getAllFieldValue(new Object[] {"Good", new Tester(2L, null)}, getParameterNames(methodF),
                Sets.newHashSet("id", "name")));


        Method methodG = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodG", Map.class);
        System.out.println(getFieldValue(new Object[] {map}, getParameterNames(methodG), "name"));
        System.out.println(getAllFieldValue(new Object[] {map}, getParameterNames(methodG),
                Sets.newHashSet("name", "count")));

        Method methodH = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodH", Map.class, Tester.class);
        System.out.println(getFieldValue(new Object[] {map, new Tester(2L, "Jerry")}, getParameterNames(methodH), "name"));
        System.out.println(getAllFieldValue(new Object[] {map, new Tester(2L, "Jerry")}, getParameterNames(methodH),
                Sets.newHashSet("id", "name", "count")));

        List<Tester> testerList = Arrays.asList(new Tester(10L, "张三"), new Tester(20L, "李四"));
        List<Tester> testerList2 = Arrays.asList(new Tester(10L, null), new Tester(20L, "李四"));

        Method methodI = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodI", String.class, List.class);
        System.out.println(getFieldValue(new Object[] {"隔壁老王", testerList}, getParameterNames(methodI), "name"));
        System.out.println(getFieldValue(new Object[] {"隔壁老王", testerList2}, getParameterNames(methodI), "name"));
        System.out.println(getFieldValue(new Object[] {null, testerList}, getParameterNames(methodI), "name"));
        System.out.println(getFieldValue(new Object[] {null, testerList2}, getParameterNames(methodI), "name"));

        Method methodJ = ReflectionUtils.findMethod(FieldAccessUtils.class, "methodJ", List.class, String.class);
        System.out.println(getFieldValue(new Object[] {testerList, "隔壁老王"}, getParameterNames(methodJ), "name"));
    }

    void methodA(List<String> list) {

    }

    void methodB(Set<String> set) {

    }

    void methodC(String[] array) {

    }

    void methodD(String value) {

    }

    void methodE(String value1, Long value2) {

    }

    void methodF(String name, Tester tester) {

    }

    void methodG(Map<String, Object> map) {

    }

    void methodH(Map<String, Object> map, Tester tester) {

    }

    void methodI(String name, List<Tester> testerList) {

    }

    void methodJ(List<Tester> testerList, String name) {

    }

    static class Tester {
        private Long id;
        private String name;

        Tester(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Tester{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}
