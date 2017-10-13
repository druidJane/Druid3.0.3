package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.MmsContent;
import com.xuanwu.msggate.common.sbi.entity.Ticket;

import java.util.ArrayList;
import java.util.List;

public class MmsFrame {

	public static final int MASS_SEND = 1;
	public static final int GROUP_SEND = 2;

	private int sendType;
	private int failedTimes;
	private MmsContent mmsContent;
	private List<Ticket> ticketList;

	public MmsFrame(int sendType, MmsContent mmsContent,
			List<Ticket> ticketList) {
		this.sendType = sendType;
		this.mmsContent = mmsContent;
		this.ticketList = ticketList;
	}

	public static MmsFrame createMassMmsFrame(MmsContent mmsContent) {
		return new MmsFrame(MASS_SEND, mmsContent, new ArrayList<Ticket>());
	}

	public static MmsFrame createGroupMmsFrame() {
		return new MmsFrame(GROUP_SEND, null, new ArrayList<Ticket>(1));
	}

	public int getSendType() {
		return sendType;
	}
	
	public List<Ticket> getTicketList() {
		return ticketList;
	}

	public void addTicket(Ticket ticket) {
		ticketList.add(ticket);
	}

	public void addTickets(List<Ticket> tickets) {
		ticketList.addAll(tickets);
	}

	public int getFailedTimes() {
		return failedTimes;
	}

	public void addFailedTimes() {
		failedTimes++;
	}

	public MmsContent getMmsContent() {
		return (sendType == MASS_SEND) ? mmsContent : ticketList.get(0)
				.getMmsContent();
	}
}
