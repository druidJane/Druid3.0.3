package com.xuanwu.mos.config.impl;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.ShardingSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Description
 * @Data 2013-9-16
 * @Version 1.0.0
 */
@Component
public class DefaultShardingSupport implements ShardingSupport {

    @Autowired
    private Config config;

    @Override
    public List<Date[]> splitByDay(Date beginTime, Date endTime) {
        List<Date[]> dateList = new ArrayList<Date[]>();
        List<Date> datePoints = splitByDayPoint(beginTime, endTime);
        if (datePoints.size() <= 2) {
            dateList.add(new Date[]{beginTime, endTime});
            return dateList;
        }
        for (int i = 0; i < datePoints.size() - 1; i++) {
            dateList.add(new Date[]{datePoints.get(i), new Date(datePoints.get(i + 1).getTime() - 1)});
        }
        dateList.get(dateList.size() - 1)[1] = endTime;
        return dateList;
    }

    private List<Date> splitByDayPoint(Date beginTime, Date endTime) {
        List<Date> dateList = new ArrayList<Date>();
        dateList.add(beginTime);
        if (endTime.getTime() - beginTime.getTime() <= 0) {
            dateList.add(endTime);
            return dateList;
        }
        if (!config.shardingTableOn(beginTime)) {
            dateList.add(endTime);
            return dateList;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginTime);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.clear();
        cal.set(year, month, day);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long time = cal.getTimeInMillis();
        long end = endTime.getTime();
        while (end - time > 0) {
            dateList.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            time = cal.getTimeInMillis();
        }
        dateList.add(endTime);
        return dateList;
    }
}
