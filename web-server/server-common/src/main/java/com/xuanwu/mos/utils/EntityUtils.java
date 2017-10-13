package com.xuanwu.mos.utils;

import com.xuanwu.mos.domain.entity.BaseEntity;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by Jiang.Ziyuan on 2017/3/29.
 */
public class EntityUtils {
    public static final List<BaseEntity> EMPTY_ENTITYS = new EmptyList();

    /**
     * @serial include
     */
    private static class EmptyList extends AbstractList<BaseEntity> implements
            RandomAccess, Serializable {
        private static final long serialVersionUID = 1008253288771183091L;

        public int size() {
            return 0;
        }

        public boolean contains(Object obj) {
            return false;
        }

        public BaseEntity get(int index) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        // Preserves singleton property
        private Object readResolve() {
            return EMPTY_ENTITYS;
        }
    }
}
