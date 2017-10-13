package com.xuanwu.mos.utils;

import com.xuanwu.mos.domain.entity.SubContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang.Ziyuan on 2017/3/24.
 */
public class DynVarUtil {
    /**
     * 短信内容中动态变量的处理，返回切分后的子短信内容<p>
     *
     * <pre>
     * <h3>示例：</h3>
     * <b>变量标签：</b>开始标签：[$，结束标签：$]
     * <b>原短信内容：</b>任意内容1<b>[$变量1$]</b>任意内容2<b>[$变量2$]</b>任意内容3
     * <b>处理结果：</b>
     *  任意内容1（SubType.TXT）
     *  变量1 （SubType.VAR）
     *  任意内容2（SubType.TXT）
     *  变量2（SubType.VAR）
     *  任意内容3（SubType.TXT）
     * </pre>
     * @param origin 原始短信内容（可能包含动态变量）
     * @param openTag 变量开始标签
     * @param closeTag 变量结束标签
     * @return
     */
    public static List<SubContent> handleDynVars(String origin, String openTag, String closeTag) {
        if(origin == null)
            throw new  NullPointerException("origin");
        if(openTag == null || closeTag == null)
            throw new NullPointerException("openTag or endTag");
        int openIdx = 0, closeIdx = 0;
        List<SubContent> subs = new ArrayList<SubContent>();
        while(true){
            openIdx = origin.indexOf(openTag, closeIdx);
            if(openIdx == -1){
                subs.add(new SubContent(SubContent.SubType.TXT, origin.substring(closeIdx)));
                break;
            }
            if(openIdx > closeIdx){
                subs.add(new SubContent(SubContent.SubType.TXT, origin.substring(closeIdx, openIdx)));
            }
            closeIdx = origin.indexOf(closeTag, openIdx);
            if(closeIdx == -1){
                subs.add(new SubContent(SubContent.SubType.TXT, origin.substring(openIdx)));
                break;
            }
            openIdx += openTag.length();
            subs.add(new SubContent(SubContent.SubType.VAR, origin.substring(openIdx, closeIdx)));
            closeIdx += closeTag.length();
            if(closeIdx == origin.length()){
                break;
            }
        }
        return subs;
    }

    public static boolean hasDynVars(List<SubContent> subs){
        if(ListUtil.isBlank(subs))
            return false;
        for(SubContent sub : subs){
            if(sub.getType() == SubContent.SubType.VAR){
                return true;
            }
        }
        return false;
    }

    public static String getVarValue(Map<String, Integer> contactMap, String key, String[] srcArr){
        if(contactMap == null)
            return null;
        Integer val = contactMap.get(key);
        if(val == null)
            return null;
        int idx = val.intValue();
        if(idx > srcArr.length - 1){
            return null;
        }
        return srcArr[idx];
    }
}
