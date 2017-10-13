package com.xuanwu.mos.utils;

import com.xuanwu.mos.dto.QueryParameters;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by 郭垚辉 on 2017/7/6.
 */
public class ObjectUtil {

    /**
     * 判断一个object是否为空
     * @param object
     * @return 为空：false ， 不为空 true
     */
    public static boolean hasValue(Object object) {
        if (null == object) {
            return false;
        }

        if (object instanceof List) {
            if (((List) object).size() <= 0) {
                return false;
            }
        } else if (object instanceof String) {
            if (StringUtils.isBlank((String) object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param query
     * @param args
     * @return
     */
    public static boolean validAllArgs(QueryParameters query, ValidType type, String... args) {
        int length = args.length;
        Map<String, Object> params = query.getParams();
        boolean resultFlag = true;

        if (type  == ValidType.ALL_NEED) {
            // 所有的参数都必须要不为空
            for (int i = 0; i < length; i++) {
                resultFlag &= ObjectUtil.hasValue(params.get(args[i]));
            }
        } else if (type  == ValidType.ONLY_ONE) {
            // 至少有一个参数不为空
            resultFlag = false;
            for (int i = 0; i < length; i++) {
                resultFlag |= ObjectUtil.hasValue(params.get(args[i]));
            }
        }
        return resultFlag;
    }

    /**
     * args的字符串数组中每一个值为key，寻找该key在QueryParameters中的值是否为空
     *
     * @return 存在至少一个key在QueryParameters不为空，则返回true，否则返回false
     */
    public static boolean validOnlyOne(QueryParameters query, String... args) {
        return ObjectUtil.validAllArgs(query, ValidType.ONLY_ONE, args);
    }

    /**
     * args的字符串数组中每一个值为key，寻找该key在QueryParameters中的值是否为空
     *
     * @return 所有的key在QueryParameters不为空，则返回true，否则返回false
     */
    public static boolean validAllArgs(QueryParameters query, String... args) {
        return ObjectUtil.validAllArgs(query, ValidType.ALL_NEED, args);
    }

    private enum ValidType {
        ONLY_ONE(1), ALL_NEED(2);
        int value;

        ValidType(int value) {
            this.value = value;
        }
    }
}
