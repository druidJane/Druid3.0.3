/*
 * Copyright (c) 2011 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.xuanwu.msggate.common.sbi.entity.MsgFrame;
import com.xuanwu.msggate.common.sbi.entity.Ticket;
import com.xuanwu.msggate.common.sbi.entity.Ticket.SMSType;
import com.xuanwu.msggate.common.sbi.entity.Ticket.TicketState;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2011-6-21
 * @Version 1.0.0
 */
public class FrameWrapper {
	public class FrameEnvoloper {
		private int msgCount;
		private MsgFrame msgFrame;
		private List<Ticket> ticketList = new ArrayList<Ticket>();

		public FrameEnvoloper(MsgFrame msgFrame) {
			this.msgFrame = msgFrame;

			for (Ticket ticket : msgFrame.getTickets()) {
				if(ticket.getState() != TicketState.WAIT)
					continue;
				
				if (ticket.getSmsType().equals(SMSType.NSMS)) {
					ticketList.add(ticket);
				} else if (ticket.getSmsType().equals(SMSType.WP)) {
					ticketList.add(ticket);
				} else if (ticket.getSmsType().equals(SMSType.MMS)) {
					ticketList.add(ticket);
				}

				if (!ticket.getSubTickets().isEmpty()) {
					for(Ticket subTicket : ticket.getSubTickets()){
						if(subTicket.getState() != TicketState.WAIT)
							continue;
						ticketList.add(ticket);
					}
				}
			}

			this.msgCount = ticketList.size();
		}

		public int getMsgCount() {
			return msgCount;
		}

		public void setMsgCount(int msgCount) {
			this.msgCount = msgCount;
		}

		public MsgFrame getMsgFrame() {
			return msgFrame;
		}

		public List<Ticket> getTicketList() {
			return ticketList;
		}

		public void setMsgFrame(MsgFrame msgFrame) {
			this.msgFrame = msgFrame;
		}

		public void decrement() {
			lock.lock();
			try {
				msgCount--;
			} finally {
				lock.unlock();
			}
		}

		public boolean isHandled() {
			return (msgCount > 0) ? false : true;
		}
	}

	private final ReentrantLock lock = new ReentrantLock();
	private final Map<Long, FrameEnvoloper> frameEnvoloperMap = new ConcurrentHashMap<Long, FrameEnvoloper>();

	public FrameEnvoloper putTicket(Ticket ticket, MsgFrame frame) {
		FrameEnvoloper frameEnvop = frameEnvoloperMap.get(ticket.getFrameID());
		if(frameEnvop == null){
			frameEnvop = new FrameEnvoloper(frame);
			frameEnvoloperMap.put(frame.getId(), frameEnvop);
		}
		frameEnvop.decrement();
		if (frameEnvop.isHandled()) {
			frameEnvoloperMap.remove(ticket.getFrameID());
			return frameEnvop;
		}
		return null;
	}

	public MsgFrame getMsgFrame(Long frameID) {
		return frameEnvoloperMap.get(frameID).getMsgFrame();
	}
}
