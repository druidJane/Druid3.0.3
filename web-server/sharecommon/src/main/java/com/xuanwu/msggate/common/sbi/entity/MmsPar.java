package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.sbi.entity.impl.PMediaItem;

/**
 * 彩信smil里的帧
 * 
 * @author <a href="liangyuanming@139130.net">Farming Liang</a>
 * @Date 2011-12-16
 */
public class MmsPar {
	public MmsPar() {
	}

	public MmsPar(int orderNo, int dur, MediaItem text, MediaItem img,
			MediaItem audio) {
		this.orderNo = orderNo;
		this.dur = dur;
		this.text = text;
		this.img = img;
		this.audio = audio;
	}

	/**
	 * 序号
	 */
	private int orderNo = 0;
	/**
	 * 帧播放间隔时间
	 */
	private int dur = 5;
	/**
	 * 文本内容
	 */
	private MediaItem text;
	/**
	 * 图片内容
	 */
	private MediaItem img;
	/**
	 * 声音内容
	 */
	private MediaItem audio;

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getDur() {
		return dur;
	}

	public void setDur(int dur) {
		this.dur = dur;
	}

	public MediaItem getText() {
		return text;
	}

	public void setText(MediaItem text) {
		this.text = text;
	}

	public MediaItem getImg() {
		return img;
	}

	public void setImg(MediaItem img) {
		this.img = img;
	}

	public MediaItem getAudio() {
		return audio;
	}

	public void setAudio(MediaItem audio) {
		this.audio = audio;
	}

	public com.xuanwu.msggate.common.protobuf.CommonItem.MmsPar build() {
		com.xuanwu.msggate.common.protobuf.CommonItem.MmsPar.Builder builder = com.xuanwu.msggate.common.protobuf.CommonItem.MmsPar
				.newBuilder();
		builder.setOrderNo(orderNo);
		builder.setDur(dur);
		if (text != null)
			builder.setText(text.build());
		if (img != null)
			builder.setImage(img.build());
		if (audio != null)
			builder.setAudio(audio.build());

		return builder.build();
	}

	public static MmsPar parseFrom(
			com.xuanwu.msggate.common.protobuf.CommonItem.MmsPar builder) {
		MmsPar mmsPar = new MmsPar();
		mmsPar.setDur(builder.getDur());
		mmsPar.setOrderNo(builder.getOrderNo());

		if (builder.getText() != null && !builder.getText().getData().isEmpty())
			mmsPar.setText(parseMediaItem(builder.getText()));

		if (builder.getImage() != null
				&& !builder.getImage().getData().isEmpty())
			mmsPar.setImg(parseMediaItem(builder.getImage()));

		if (builder.getAudio() != null
				&& !builder.getAudio().getData().isEmpty())
			mmsPar.setAudio(parseMediaItem(builder.getAudio()));

		return mmsPar;
	}

	private static MediaItem parseMediaItem(
			com.xuanwu.msggate.common.protobuf.CommonItem.MediaItem builder) {
		return new PMediaItem(builder.getMediaType(), builder.getMeta(),
				builder.getData().toByteArray());
	}
}
