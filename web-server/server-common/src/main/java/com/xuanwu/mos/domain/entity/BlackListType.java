package com.xuanwu.mos.domain.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangz on 2017/4/21.
 */
public enum BlackListType {
        Illegal(-1), User(0), Enterprise(2), CHANNEL(4), BizType(5);

        public int index;

        private BlackListType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static BlackListType getType(int index) {
            switch (index) {
                case 0:
                    return User;
                case 2:
                    return Enterprise;
                case 4:
                    return CHANNEL;
                case 5:
                    return BizType;
                default:
                    return Illegal;
            }
        }

        public static BlackListType getType(String typeName) {
            if (StringUtils.isEmpty(typeName)) {
                return Illegal;
            }

            String tempTypeName = typeName.trim();
            if ("用户".equals(tempTypeName)) {
                return User;
            } else if ("企业".equals(tempTypeName)) {
                return Enterprise;
            } else if ("通道".equals(tempTypeName)) {
                return CHANNEL;
            } else if ("业务类型".equals(tempTypeName)) {
                return BizType;
            } else {
                return Illegal;
            }
        }

        public static String getTypeName(int type) {
            switch (type) {
                case 0:
                    return "用户";
                case 2:
                    return "企业";
                case 4:
                    return "通道";
                case 5:
                    return "业务类型";
                default:
                    return "企业";
            }
        }
}
