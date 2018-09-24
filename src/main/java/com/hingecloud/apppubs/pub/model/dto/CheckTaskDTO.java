package com.hingecloud.apppubs.pub.model.dto;

import com.hingecloud.apppubs.pub.tools.ValidateHelper;

public class CheckTaskDTO extends AbsDTO {
    private Integer taskId;

    @Override
    public void validate() {
        ValidateHelper.notNull(taskId, "taskId不存在！");
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
