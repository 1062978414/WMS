<%--
  Created by IntelliJ IDEA.
  User: DL
  Date: 2019/5/13
  Time: 16:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script>
    var search_type_Video = "none";
    var search_keyWord = "";
    var selectID;
    var NAMEBYUUID;

    //初始化事件
    $(function () {
        videoListInit();
        searchAction();
        optionAction();
        changeDateFormat();
        addVideoAction();
        bootstrapValidatorInit();
        //0.初始化fileinput
                var oFileInput = new FileInput();
                oFileInput.Init("myfile", "videoManage/addFile");

    })

    //获取当前时间方法，可共用

    function getCurDate() {
        var date = new Date();
        //年
        var year = date.getFullYear();
        if(year.length==1){
            year = "0"+""+year;
        }
        //月
        var month = date.getMonth() + 1;
        if(month.length==1){
            month = "0"+""+month;
        }
        //日
        var day = date.getDate();
        if(day.length==1){
            day = "0"+""+day;
        }
        //时
        var hh = date.getHours();
        if(hh.length==1){
            hh = "0"+""+hh;
        }
        //分
        var mm = date.getMinutes();
        if(mm.length==1){
            mm = "0"+""+mm;
        }
        //秒
        var ss = date.getSeconds();
        if(ss.length==1){
            ss = "0"+""+ss;
        }
        var rq = year + "-" + month + "-" + day + "%" + hh + ":" + mm + ":" + ss;
        return rq;
    }
        // 添加视频模态框数据校验
    function bootstrapValidatorInit() {
        $("#videoAdd_form").bootstrapValidator({
            message: 'This is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            excluded: [':disabled'],
            fields: {
                name: {
                    validators: {
                        notEmpty: {
                            message: '视频名称不能为空'
                        }
                    }
                },
                info: {
                    validators: {
                        notEmpty: {
                            message: '视频描述不能为空'
                        }
                    }
                },
                myfile: {
                    validators: {
                        notEmpty: {
                            message: '请上传视频'
                        }
                    }
                }
            }
        })
    }


    function FileInput() {
        var oFile = new Object();
        //初始化fileinput控件（第一次初始化）
        oFile.Init = function (ctrlName, uploadUrl) {
            var control = $('#' + ctrlName);

            //初始化上传控件的样式
            control.fileinput({
                language: 'zh', //设置语言
                uploadUrl: uploadUrl, //上传的地址
                //allowedFileExtensions: ['avi ','rmvb ','asf ','3gp ','mpg ','mpeg ','mpe ','wmv ','mp4 ','mkv ','vob'],//接收的文件后缀
                showUpload: false, //是否显示上传按钮
                autoReplace : true,//自动替换上一个文件
                showCaption: true,//是否显示标题
                browseClass: "btn btn-primary", //按钮样式
                //dropZoneEnabled: false,//是否显示拖拽区域
                //minImageWidth: 50, //图片的最小宽度
                //minImageHeight: 50,//图片的最小高度
                //maxImageWidth: 1000,//图片的最大宽度
                //maxImageHeight: 1000,//图片的最大高度
                maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
                //minFileCount: 0,
                maxFileCount: 1, //表示允许同时上传的最大文件个数
                enctype: 'multipart/form-data',
                validateInitialCount: true,
                previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
                msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
            }).on("filebatchselected", function(event, files) {
                $(this).fileinput("upload");
            })  .on("fileuploaded", function(event, data) {
                if(data.response)
                {
                    NAMEBYUUID=data.response.filename;
                }
            });

        }

        return oFile;
    };


    // 添加视频信息
    function addVideoAction() {
        $('#add_Video').click(function () {
            $('#add_modal').modal("show");
            //重置校验状态
                $("#videoAdd_form").data('bootstrapValidator').destroy();
                $('#videoAdd_form').data('bootstrapValidator', null);
                bootstrapValidatorInit();

        });
        //新增窗口提交按钮点击事件
        $('#add_modal_submit').click(function () {
            //实现验证成功后提交
            debugger
            var bootstrapValidator = $("#videoAdd_form").data('bootstrapValidator');
            //手动触发验证
            bootstrapValidator.validate();
            if (bootstrapValidator.isValid()) {
                debugger
                var data = {
                    name: $('#name').val(),
                    info: $('#info').val(),
                    realfilename: $('.file-caption-name').val(),
                    namebyuuid: NAMEBYUUID
            }
                // ajax
                $.ajax({
                    type: "POST",
                    url: "videoManage/addVideo",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function (response) {
                        $('#add_modal').modal("hide");
                        var msg;
                        var type;
                        var append = '';
                        if (response.result == "success") {
                            type = "success";
                            msg = "视频添加成功";
                        } else if (response.result == "error") {
                            type = "error";
                            msg = "视频添加失败";
                        }
                        $("#name").val("");
                        $("#info").val("");
                        $(".fileinput-remove").click()
                        showMsg(type, msg, append);
                        tableRefresh();
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        $('#add_modal').modal("hide");
                        // handle error
                        handleAjaxError(xhr.status);
                    }
                })

            }
            //	})
        })
    }

    //查询类型下拉选择操作
    function optionAction() {
        $(".dropOption").click(function () {
            var type = $(this).text();
            $("#search_input").val("");
            if (type == "视频ID") {
                search_type_Video = "searchByID";
                $(".dateRange").hide()
                $(".inputCon").show();
            } else if (type == "视频名称") {
                search_type_Video = "searchByName";
                $(".dateRange").hide()
                $(".inputCon").show();
            } else if (type == "创建人") {
                search_type_Video = "searchByCreateUser";
                $(".dateRange").hide();
                $(".inputCon").show();
            } else if (type == "创建时间") {
                search_type_Video = "searchByCreateTime";
                $(".dateRange").show();
                $(".inputCon").hide();
            }
            $("#search_type").text(type);
            $("#search_input").attr("placeholder", type);
        })
    }

    //修改——转换日期格式(时间戳转换为datetime格式)
    function changeDateFormat(cellval) {
        if (cellval != null) {
            var date = new Date(parseInt(cellval));
            var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            //debugger
            var hh24 = date.getHours();
            var mi = date.getMinutes();
            var ss = date.getSeconds();
            if (hh24.toString().length == 1)
                hh24 = "0" + "" + hh24;
            if (mi.toString().length == 1)
                mi = "0" + "" + mi;
            return date.getFullYear() + "-" + month + "-" + currentDate + " " + hh24 + ":" + mi;
        }
    }


    // 搜索动作
    function searchAction() {
        $('#search_button').click(function () {
            search_keyWord = $('#search_input').val();
            tableRefresh();
        })
    }

    // 分页查询参数
    function queryParams(params) {
        var temp = {
            limit: params.limit,
            offset: params.offset,
            searchType: search_type_Video,
            keyWord: search_keyWord
        }
        return temp;
    }

    // 表格初始化
    function videoListInit() {
        $('#videoList')
            .bootstrapTable(
                {
                    columns: [
                        {
                            field: 'id',
                            title: '视频ID'
                            //sortable: true
                        },
                        {
                            field: 'name',
                            title: '名称'
                        },
                        {
                            field: 'info',
                            title: '视频描述'
                        },
                        {
                            field: 'createdate',
                            title: '上传时间',
                            formatter: function (value, row, index) {
                                return changeDateFormat(value)
                            }
                        },
                        {
                            field: 'createuser',
                            title: '创建人'
                        },
                        {
                            field: 'num',
                            title: '访问量'
                        },
                        {
                            field: 'operation',
                            title: '操作',
                            formatter: function (value, row, index) {
                                var s = '<button class="btn btn-info btn-sm edit"><span>编辑</span></button>';
                                var d = '<button class="btn btn-danger btn-sm delete"><span>删除</span></button>';
                                var fun = '';
                                return s + ' ' + d;
                            },
                            events: {
                                // 操作列中编辑按钮的动作
                                'click .edit': function (e, value,
                                                         row, index) {
                                    selectID = row.id;
                                    rowEditOperation(row);
                                },
                                'click .delete': function (e,
                                                           value, row, index) {
                                    selectID = row.id;
                                    $('#deleteWarning_modal').modal(
                                        'show');
                                },
                                'click .resetpw': function (e,
                                                            value, row, index) {
                                    selectID = row.id;
                                    $('#resetpwWarning_modal').modal(
                                        'show');
                                }
                            }
                        }],
                    url: 'videoManage/getVideoList',
                    onLoadError: function (status) {
                        handleAjaxError(status);
                    },
                    method: 'GET',
                    queryParams: queryParams,
                    sidePagination: "server",
                    dataType: 'json',
                    pagination: true,
                    pageNumber: 1,
                    pageSize: 5,
                    pageList: [5, 10, 25, 50, 100],
                    clickToSelect: true
                });
    }

    // 表格刷新
    function tableRefresh() {
        $('#videoList').bootstrapTable('refresh', {
            query: {}
        });
    }

</script>
<html>
<head>
    <style>
        .btn-file {
            margin-top: 0px;
        }
    </style>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/js/fileupload/css/fileinput.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/fileupload/js/fileinput.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/fileupload/js/locales/zh.js"></script>
    <title>视频管理</title>
</head>
<body>

<!-- 添加视频模态框 -->
<div id="add_modal" class="modal fade" table-index="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" type="button" data-dismiss="modal"
                        aria-hidden="true">&times;
                </button>
                <h4 class="modal-title" id="myModalLabel">添加视频信息</h4>
            </div>
            <div class="modal-body">
                <!-- 模态框的内容 -->
                <div class="row">
                    <div class="col-md-1 col-sm-2"></div>
                    <div class="col-md-8 col-sm-8">
                        <form class="form-horizontal" role="form" id="videoAdd_form"
                              style="margin-top: 25px">
                            <div class="form-group">
                                <label for="" class="control-label col-md-5 col-sm-5"> <span>视频名称：</span>
                                </label>
                                <div class="col-md-7 col-sm-7">
                                    <input type="text" class="form-control" id="name"
                                           name="name" placeholder="视频名称">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="" class="control-label col-md-5 col-sm-5"> <span>视频描述：</span>
                                </label>
                                <div class="col-md-7 col-sm-7">
                                    <input type="text" class="form-control" id="info"
                                           name="info" placeholder="视频描述">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="" class="control-label col-md-5 col-sm-5"> <span>视频数据：</span>
                                </label>
                                <br>
                                <div style="float: right;margin-top: 15px" class="col-md-10 col-sm-10">
                                    <input id="myfile" name="myfile" class="file" type="file" data-show-caption="true">
                                    <%-- <p class="help-block">支持jpg、jpeg、png格式，大小不超过2.0M</p>--%>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="col-md-1 col-sm-1"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-default" type="button" data-dismiss="modal">
                    <span>取消</span>
                </button>
                <button class="btn btn-success" type="button" id="add_modal_submit">
                    <span>提交</span>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="panel panel-default">
    <ol class="breadcrumb">
        <li>视频管理</li>
    </ol>
    <div class="panel-body">
        <div class="row">
            <div class="col-md-8 col-sm-8">
                <div class="row">
                    <div class="col-md-2 col-sm-3">
                        <div class="btn-group">
                            <button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                <span id="search_type">查询方式</span><span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="javascript:void(0)" class="dropOption">视频ID</a></li>
                                <li><a href="javascript:void(0)" class="dropOption">视频名称</a></li>
                                <li><a href="javascript:void(0)" class="dropOption">创建人</a></li>
                                <li><a href="javascript:void(0)" class="dropOption">创建时间</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-md-10 col-sm-10">
                        <div>
                            <div class="col-md-3 col-sm-5 inputCon">
                                <input id="search_input" type="text" class="form-control" placeholder="查询视频信息">
                            </div>

                            <div class="col-md-7 dateRange" hidden="hidden">
                                <form action="" class="form-inline">
                                    <input style="width: 120px" class="form_date form-control" value=""
                                           id="search_start_date" name="" placeholder="开始日期">
                                    <label class="form-label">&nbsp;-&nbsp;</label>
                                    <input style="width: 120px" class="form_date form-control" value=""
                                           id="search_end_date" name="" placeholder="结束日期">
                                </form>
                            </div>
                            <div class="col-md-2 col-sm-5">
                                <button id="search_button" class="btn btn-success">
                                    <span class="glyphicon glyphicon-search"></span><span>查询</span>
                                </button>
                            </div>
                            <div class="col-md-2 col-sm-5">
                                <button id="add_Video" class="btn btn-default">
                                    <span class="glyphicon glyphicon-plus"></span><span>添加视频</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%--<div class="row" style="margin-top: 25px">
            <div class="col-md-5">
                <button class="btn btn-sm btn-default" id="add_Video">
                    <span class="glyphicon glyphicon-plus">添加视频</span>
                </button>
            </div>
            <div class="col-md-5"></div>
        </div>--%>

        <div class="row" style="margin-top: 15px">
            <div class="col-md-12">
                <table id="videoList" class="table table-striped"></table>
            </div>
        </div>
    </div>
</div>

</body>
</html>
