package com.xuanwu.msggate.common.sbi.entity;

import com.xuanwu.msggate.common.zip.ZipUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 彩信附件内容
 * 
 * @author <a href="liangyuanming@139130.net">Farming Liang</a>
 * @Date 2011-12-16
 */
public class MmsContent {
	public MmsContent() {
		this.mmsState = MmsState.NORMAL;
	}
	public enum MmsState{
		NORMAL(0),ERROR(1),ILLEGALKEY(2),BLANKCONTENT(3);
		
		private final int index;
		
		private MmsState(int index){
			this.index = index;
		}
		
		public int getIndex(){
			return index;
		}
		public static MmsState getMmsState(int index) {
			MmsState[] mmsStates = MmsState.values();
			for (MmsState mmsState : mmsStates) {
				if(mmsState.getIndex() == index)
					return mmsState;
			}
			return NORMAL; 
		}

		public static MmsState parseEnum(String str) {
			return MmsState.valueOf(str);
		}
	}
	public MmsContent(String smil, String subject, List<MmsPar> mmsPars) {
		this.smil = smil;
		this.subject = subject;
		this.mmsPars = mmsPars;
	}
	/**
	 * 彩信的状态
	 */
	private MmsState mmsState;
	/**
	 * 彩信smil+内容
	 */
	private String content;

	/**
	 * smil
	 */
	private String smil;
	/**
	 * 总大小
	 */
	private int size;

	/**
	 * 类型
	 */
	private Set<String> types;

	/**
	 * 标题
	 */
	private String subject;

	/**
	 * 彩信帧
	 */
	private List<MmsPar> mmsPars;

	public void setMmsState(MmsState mmsState) {
		this.mmsState = mmsState;
	}

	public MmsState getMmsState() {
		return mmsState;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public String getSmil() {
		return smil;
	}

	public void setSmil(String smil) {
		this.smil = smil;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Set<String> getTypes() {
		return types;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<MmsPar> getMmsPars() {
		return mmsPars;
	}

	public void setMmsPars(List<MmsPar> mmsPars) {
		this.mmsPars = mmsPars;
	}

	public int getSize() {
		return size;
	}

	public byte[] getData() {
		try {
			com.xuanwu.msggate.common.protobuf.CommonItem.MmsContent.Builder builder = com.xuanwu.msggate.common.protobuf.CommonItem.MmsContent
					.newBuilder();
			builder.setSmil(smil);
			builder.setSubject(subject);

			if (mmsPars != null) {
				for (MmsPar mmsPar : mmsPars) {
					builder.addMmsPars(mmsPar.build());
				}
			}
			return ZipUtil.zipByteArray(builder.build().toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static MmsContent parseFrom(byte[] data) {
		try {
			com.xuanwu.msggate.common.protobuf.CommonItem.MmsContent builder = com.xuanwu.msggate.common.protobuf.CommonItem.MmsContent
					.parseFrom(ZipUtil.unzipByteArray(data));
			MmsContent mmsContent = new MmsContent();
			mmsContent.setSubject(builder.getSubject());
			mmsContent.setSmil(builder.getSmil());

			if (builder.getMmsParsList() != null) {
				List<MmsPar> tempList = new ArrayList<MmsPar>();
				for (com.xuanwu.msggate.common.protobuf.CommonItem.MmsPar item : builder
						.getMmsParsList()) {
					tempList.add(MmsPar.parseFrom(item));
				}
				mmsContent.setMmsPars(tempList);
			}
			return mmsContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
