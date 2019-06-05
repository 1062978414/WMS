package com.ken.wms.common.controller;

import com.ken.wms.common.service.Interface.RepositoryAdminManageService;
import com.ken.wms.common.service.Interface.VideoManageService;
import com.ken.wms.common.util.Response;
import com.ken.wms.common.util.ResponseFactory;
import com.ken.wms.domain.RepositoryAdmin;
import com.ken.wms.domain.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/**/videoManage")
public class VideoManagementHandler {

    @Autowired
    private VideoManageService videoManageService;

    // 查询类型
    private static final String SEARCH_BY_ID = "searchByID";
    private static final String SEARCH_BY_NAME = "searchByName";
    private static final String SEARCH_BY_CRATEUSER = "searchByCreateUser";
    private static final String SEARCH_BY_CRATETIME = "searchByCreateTime";
    private static final String SEARCH_ALL = "searchAll";


    /**
     * 通用记录查询
     *
     * @param keyWord    查询关键字
     * @param searchType 查询类型
     * @param offset     分页偏移值
     * @param limit      分页大小
     * @return 返回所有符合条件的记录
     */
    private Map<String, Object> query(String keyWord, String searchType, int offset, int limit) {
        Map<String, Object> queryResult = null;

        //查询方式判断分支

        switch (searchType) {
            case SEARCH_ALL:
                queryResult = videoManageService.selectAll(offset, limit);
                break;
            case "none":
                queryResult = videoManageService.selectAll(offset, limit);
                break;
            case SEARCH_BY_ID:
                break;
            case SEARCH_BY_NAME:
                break;
            case SEARCH_BY_CRATEUSER:
                break;
            case SEARCH_BY_CRATETIME:
                break;
            default:
                break;
        }
        return queryResult;
    }


    /**
     * 查询仓库管理员信息
     *
     * @param searchType 查询类型
     * @param offset     分页偏移值
     * @param limit      分页大小
     * @param keyWord    查询关键字
     * @return 返回一个Map，其中key=rows，表示查询出来的记录；key=total，表示记录的总条数
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getVideoList", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getVideoList(@RequestParam("searchType") String searchType,
                                     @RequestParam("keyWord") String keyWord, @RequestParam("offset") int offset,
                                     @RequestParam("limit") int limit) {
        Response responseContent = ResponseFactory.newInstance();
        List<Video> rows = null;
        long total = 0;
        // 查询
        Map<String, Object> queryResult = query(keyWord,searchType,offset,limit);

        if (queryResult != null) {
            rows = (List<Video>) queryResult.get("data");
            total = (long) queryResult.get("total");
        }

        // 设置 Response
        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }

    @RequestMapping(value="/addFile",method={RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> addImg(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> result= new HashMap<String, Object>();

        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file= multipartRequest.getFile("myfile");
        //自动生成视频ID
        String videoId = UUID.randomUUID().toString();
        try{
            if(!(file.getOriginalFilename() == null || "".equals(file.getOriginalFilename()))){
                String imgDir = "E:\\upload\\video";        // 图片上传地址
                // 对文件进行存储处理
                byte[] bytes = file.getBytes();
                Path path = Paths.get(imgDir,"\\"+videoId);
                Files.write(path,bytes);

                result.put("msg","上传成功！");
                result.put("result",true);
            }
        }catch(IOException e){
            result.put("msg","出错了");
            result.put("result",false);
            e.printStackTrace();
        }catch (Exception e1){
            e1.printStackTrace();
        }
        return result;
    }
}
