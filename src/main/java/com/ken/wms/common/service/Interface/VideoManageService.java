package com.ken.wms.common.service.Interface;

import com.ken.wms.domain.Video;
import com.ken.wms.exception.RepositoryAdminManageServiceException;

import java.util.Map;

public interface VideoManageService {
    Map<String, Object> selectAll();

    /**
     * 分页查询视频的记录
     *
     * @param offset 分页的偏移值
     * @param limit  分页的大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectAll(int offset, int limit);

    /**
     * 添加视频信息
     * @param video
     * @return 返回Boolean值， true为成功
     */

    boolean addVideo(Video video);
}
