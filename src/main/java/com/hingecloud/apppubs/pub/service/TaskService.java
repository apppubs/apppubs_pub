package com.hingecloud.apppubs.pub.service;

import com.baomidou.mybatisplus.service.IService;
import com.hingecloud.apppubs.pub.exception.CreateTaskException;
import com.hingecloud.apppubs.pub.model.TTask;
import com.hingecloud.apppubs.pub.model.dto.CheckTaskDTO;
import com.hingecloud.apppubs.pub.model.dto.CreateTaskDTO;
import com.hingecloud.apppubs.pub.model.vo.CheckTaskVO;
import com.hingecloud.apppubs.pub.model.vo.CreateTaskVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张稳
 * @since 2018-09-20
 */
public interface TaskService extends IService<TTask> {

    /**
     * 增加任务到编译队列，如果此同一个id和类型的app已经在队列里，抛出异常。
     * @param dto
     */
    CreateTaskVO addTask(CreateTaskDTO dto) throws CreateTaskException;

    CheckTaskVO checkTask(CheckTaskDTO dto);
}
