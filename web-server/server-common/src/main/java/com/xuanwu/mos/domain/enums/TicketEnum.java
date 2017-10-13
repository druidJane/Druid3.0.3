package com.xuanwu.mos.domain.enums;

import com.xuanwu.mos.domain.handler.HasIndexValue;

/** 如果ticket表中还有其他的状态，那么可以在该类中添加枚举值
 * Created by 郭垚辉 on 2017/5/23.
 */
public class TicketEnum  {

    public enum State implements HasIndexValue {
        OTHERROE(-1,"其他错误"),
        WAITSEND(0,"等待发送"),
        SUCCESS(1,"发送成功"),
        REJECT(2,"被拒绝"),
        ERRORFORMAT(3,"数据格式错误"),
        SENDOVERONE(4,"多次发送失败"),
        ENDFRAME(5,"帧结束标志"),
        ERRORSERIAL(6,"序列号错误"),
        REJECTSYSTEM(7,"系统拒绝发送");


        private int index;
        private String ticketStateName;

        State(int index, String ticketStateName) {
            this.index = index;
            this.ticketStateName = ticketStateName;
        }

        public static State getTicketStateEnum(int index) {
            for (State type : State.values()) {
                if (type.getIndex() == index) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public String getTicketStateName() {
            return this.ticketStateName;
        }
    }

}
