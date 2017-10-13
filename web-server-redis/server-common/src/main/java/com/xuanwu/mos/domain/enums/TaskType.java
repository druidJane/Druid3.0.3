package com.xuanwu.mos.domain.enums;

/**
 * Created by 林泽强 on 2016/8/26. 任务状态
 */
public enum TaskType implements HasIndexValue {

	Import(1), Export(2);

	private int index;

	private TaskType(int index) {
		this.index = index;
	}

	public static TaskType getType(int index) {
		for (TaskType type : TaskType.values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}

		return Import;
	}

	@Override
	public int getIndex() {
		return index;
	}
}
