package com.xuanwu.mos.domain.enums;

/**
 * Created by 林泽强 on 2016/8/26. 文件任务处理结果
 */
public enum TaskResult implements HasIndexValue {

	Wait(0), Success(1), Failed(2), Interrupt(3);

	private int index;

	private TaskResult(int index) {
		this.index = index;
	}

	public static TaskResult getResult(int index) {
		for (TaskResult result : TaskResult.values()) {
			if (result.getIndex() == index) {
				return result;
			}
		}
		return Failed;
	}

	@Override
	public int getIndex() {
		return index;
	}
}
