package com.xuanwu.mos.domain.entity;

import com.xuanwu.mos.domain.AbstractEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 彩信内容
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-10-30
 * @Version 1.0.0
 */
public class MmsContent {

	/** 标题 */
	private String subject;
	
	private String smil;

	/** 彩信帧列表 */
	private List<Frame> frames;
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getSmil() {
		return smil;
	}
	
	public void setSmil(String smil) {
		this.smil = smil;
	}
	
	public List<Frame> getFrames() {
		return frames;
	}
	
	public void setFrames(List<Frame> frames) {
		this.frames = frames;
	}

	/**
	 * @Description 单个彩信帧
	 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
	 * @Data 2012-10-31
	 * @Version 1.0.0
	 */
	public static class Frame {

		private String id;
		private Text text;
		private Image img;
		private Audio audio;
		private Video video;
		private int duration=5000;
		
		public int getSize(){
			int t = 0;
			if(text != null) t += text.size;
			if(img != null) t += img.size;
			if(audio != null) t += audio.size;
			if(video != null) t += video.size;
			return t;
		}
		
		public Text getText() {
			return text;
		}

		public void setText(Text text) {
			this.text = text;
		}

		public Image getImg() {
			return img;
		}

		public void setImg(Image img) {
			this.img = img;
		}

		public Audio getAudio() {
			return audio;
		}

		public void setAudio(Audio audio) {
			this.audio = audio;
		}

		public Video getVideo() {
			return video;
		}

		public void setVideo(Video video) {
			this.video = video;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}
	}

	/**
	 * @Description 文本
	 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
	 * @Data 2012-10-31
	 * @Version 1.0.0
	 */
	public static class Text {
		
		private long size;
		
		private String name;
		
		private String content;
		
		private List<SubContent> subs;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public List<SubContent> getSubs() {
			return subs;
		}

		public void setSubs(List<SubContent> subs) {
			this.subs = subs;
		}
	}

	/**
	 * @Description 图片
	 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
	 * @Data 2012-10-31
	 * @Version 1.0.0
	 */
	public static class Image {
		private long size;
		private String name;

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	/**
	 * @Description 声音
	 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
	 * @Data 2012-10-31
	 * @Version 1.0.0
	 */
	public static class Audio  extends AbstractEntity {
		private long size;
		private String name;

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public Serializable getId() {
			return null;
		}
	}

	/**
	 * @Description 视频
	 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
	 * @Data 2017-03-07
	 * @Version 1.0.0
	 */
	public static class Video {
		private long size;
		private String name;

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
