package com.ken.wms.domain;

import java.sql.Blob;
import java.util.Date;

/**
 * 视频管理
 * @author DL
 * @since 2019年5月13日17:36:35
 */
public class Video {
    private Integer id;//视频ID
    private String name;//视频名称
    private String info;//视频信息
    private Date createdate;//创建时间
    private String createuser;//创建人
    private Integer num;//访问量
    private String realfilename;//文件真实名字
    private String namebyuuid;//uuid编码文件名


    public String getNamebyuuid() {
        return namebyuuid;
    }

    public void setNamebyuuid(String namebyuuid) {
        this.namebyuuid = namebyuuid;
    }



    public String getRealfilename() {
        return realfilename;
    }

    public void setRealfilename(String realfilename) {
        this.realfilename = realfilename;
    }





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
@Override
    public String toString (){
    return "Video{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", info='" + info + '\'' +
            ", createdate=" + createdate +
            ", createuser='" + createuser + '\'' +
            ", num=" + num +
            '}';
}

}
