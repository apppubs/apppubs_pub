package com.hingecloud.apppubs.pub.model.dto;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hingecloud.apppubs.pub.exception.ArgumentCheckException;
import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.service.TaskService;
import com.hingecloud.apppubs.pub.tools.SpringContextHolder;
import com.hingecloud.apppubs.pub.tools.ValidateHelper;


public class CancelTaskDTO extends AbsDTO {
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public void validate() {
        ValidateHelper.notNull(taskId, "taskId不存在！");
        TaskService taskService = SpringContextHolder.getBean(TaskService.class);
        Wrapper wp = new EntityWrapper<TTask>();
        wp.eq("id", taskId);
        int count = taskService.selectCount(wp);
        if (count < 1) {
            throw new ArgumentCheckException(String.format("taskId=%s 任务不存在", taskId));
        }
    }
}
