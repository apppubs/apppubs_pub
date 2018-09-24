package com.hingecloud.apppubs.pub.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hingecloud.apppubs.pub.model.TTask;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 张稳
 * @since 2018-09-20
 */
public interface TaskMapper extends BaseMapper<TTask> {

    public Integer selectLatestVersionCode(@Param("appId") String appId, @Param("type") String type);

    public void updateStatus(@Param("appId") String appId, @Param("type") String type, @Param("versionCode") Integer versionCode, @Param("status") Integer status);

    public Integer add(TTask task);

}
