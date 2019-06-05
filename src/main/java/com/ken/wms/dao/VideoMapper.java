package com.ken.wms.dao;

import com.ken.wms.domain.Video;

import java.util.List;

/**
 * Video映射器
 *
 * @author DL
 */
public interface VideoMapper {
    /**
     * 选择所有视频
     * @return 返回所有视频信息
     */
    List<Video> selectAll();
}
