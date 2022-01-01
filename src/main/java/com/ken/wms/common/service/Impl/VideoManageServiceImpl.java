package com.ken.wms.common.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ken.wms.common.service.Interface.VideoManageService;
import com.ken.wms.dao.VideoMapper;
import com.ken.wms.domain.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoManageServiceImpl implements VideoManageService {

    @Autowired
    private VideoMapper videoMapper;

    /**
     * 查询所有仓库管理员的记录
     *
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @Override
    public Map<String, Object> selectAll() {
        return selectAll(-1, -1);
    }

    /**
     * 分页查询视频记录
     *
     * @param offset 分页的偏移值
     * @param limit  分页的大小
     * @return 结果的一个Mao, 其中： key 为data的代表记录数据；key为total代表结果记录的数量
     */
    @Override
    public Map<String, Object> selectAll(int offset, int limit) {
        //初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<Video> video;
        long total = 0;
        boolean isPagination = true;

        //validate
        if (offset < 0 || limit < 0) {
            isPagination = false;
        }
        //query
        if (isPagination) {
            PageHelper.offsetPage(offset, limit);
            video = videoMapper.selectAll();
            if (video != null) {
                PageInfo<Video> pageInfo = new PageInfo<>(video);
                total = pageInfo.getTotal();
            } else {
                video = new ArrayList<>();
            }
        } else {
            video = videoMapper.selectAll();
            if (video != null) {
                total = video.size();
            } else {
                video = new ArrayList<>();
            }
        }
        resultSet.put("data", video);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 添加视频信息
     *
     * @param video
     * @return 返回boolean 值
     * @since 2019年6月6日09:55:28
     */

    @Override
    public boolean addVideo(Video video) {
        if (video != null) {
            try {
                // 有效性验证
                if (videoCheck(video)) {
                    videoMapper.insert(video);
                }
                return true;
            }catch (Exception e){

            }
        }
        return false;
    }

    /**
     * 检查仓库信息是否满足
     *
     * @param video 视频信息
     * @return 若视频信息满足要求则返回true，否则返回false
     */
    private boolean videoCheck(Video video) {
        return video.getName() != null && video.getInfo() != null && video.getNamebyuuid() != null;
    }
}
