package com.hingecloud.apppubs.pub.controller;

import com.hingecloud.apppubs.pub.exception.CreateTaskException;
import com.hingecloud.apppubs.pub.model.dto.CancelTaskDTO;
import com.hingecloud.apppubs.pub.model.dto.CheckTaskDTO;
import com.hingecloud.apppubs.pub.model.vo.CheckTaskVO;
import com.hingecloud.apppubs.pub.model.vo.CreateTaskVO;
import com.hingecloud.apppubs.pub.service.TaskService;
import com.hingecloud.apppubs.pub.tools.CodeConst;
import com.hingecloud.apppubs.pub.tools.JsonResult;
import com.hingecloud.apppubs.pub.model.dto.CreateTaskDTO;
import com.qiniu.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService mTaskService;

    @PostMapping(value = "create")
    @ResponseBody
    public JsonResult addTask(CreateTaskDTO dto) {
        dto.validate();
        try {
            CreateTaskVO vo = mTaskService.addTask(dto);
            return JsonResult.success(vo,"任务添加成功！");
        } catch (CreateTaskException e) {
            e.printStackTrace();
            return JsonResult.failure(CodeConst.TASK_CREATE_ERROR,e.getMessage());
        }
    }

    @PostMapping(value="check")
    @ResponseBody
    public JsonResult checkTask(CheckTaskDTO dto){
        dto.validate();
        CheckTaskVO vo = mTaskService.checkTask(dto);
        return JsonResult.success(vo,"查询完成！");
    }

    @PostMapping("cancel")
    @ResponseBody
    public JsonResult cancel(CancelTaskDTO dto){
        dto.validate();
        mTaskService.cancelTask(dto);
        return JsonResult.success(null,"取消成功!");
    }
}
