package com.hingecloud.apppubs.pub.controller;

import com.hingecloud.apppubs.pub.tools.JsonResult;
import com.hingecloud.apppubs.pub.model.dto.CreateTaskDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskController {

    @PostMapping(value = "create")
    @ResponseBody
    public JsonResult addTask(CreateTaskDTO dto) {
        dto.validate();

        return JsonResult.success("是否完成任务：" + "是");
    }

}
