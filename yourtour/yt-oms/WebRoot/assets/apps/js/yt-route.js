
/**
 *
 * @type {{query: Function, saveDictInfo: Function, loadDictInfo: Function}}
 */
jQuery.Route = {
    init:function(){
        $.Page.show("Page_RouteListView");

        var me = this,
            listview = $("#Page_RouteListView"),
            formview = $("#Page_RouteFormView"),
            scheduleformview = $("#Page_ScheduleFormView");

        $("#btn_add", listview).on('click', function(){
            me.createRouteInfo();
        });

        $("#btn_edit", listview).on('click', function(){
            me.loadRouteInfo();
        });

        $("#btn_plan", listview).on('click', function(){
            me.loadRouteScheduleInfo();
        });

        //保存按钮事件
        $("#btn_save", formview).on('click', function(){
            me.saveRouteInfo()
        });

        //取消按钮事件
        $(" #btn_cancel", formview).on('click', function(){
            $.Page.back();
        });

        $(".schedule-indicator", scheduleformview).on("click", function(){
            alert('adfadsfads');
        });

        this.query();
    },

    createRouteInfo:function(){
        var formview = $("#Page_RouteFormView"),
            form = $("#RouteForm", formview);

        $.Page.show("Page_RouteFormView", function(){
            form.clear();

            $('#id', form).val(-1);
        });
    },

    /**
     * 列表查询
     */
    query:function(){
        var dt = $("#Page_RouteListView #datatable_route")
        dt.dataTable({
            "aoColumns": [
                {"mData": "id", "sClass":"center", "sWidth": "5%", "mRender": function (data, type, row) {
                    return "<input type='checkbox' class='checkboxes' value='" + data + "'/>";
                }},
                {
                    "mData": "name",
                    "sWidth": "25%"
                },

                {
                    "mData": "lineName",
                    "sWidth": "25%"
                },

                {
                    "mData": "duration",
                    "sWidth": "10%"
                },

                {
                    "mData": "name",
                    "sWidth": "35%"
                }
            ]
        });
    },

    showSearchView:function(callback){
        $.Page.show("Page_Route_SearchListView");

        var me = this,
            searchview = $("#Page_Route_SearchListView");

        $("#btn_ok", searchview).on('click', function(){
            $("#datatable_routes").select(function(ids){
                callback(ids);
            },"请选择行程。");
        });

        $("#btn_back", searchview).on('click', function(){
            $.Page.back();
        })
    },

    search:function(){
        var dt = $("#Page_Route_SearchListView #datatable_routes");

        dt.dataTable({
            "aoColumns": [
                {"mData": "id", "sClass":"center", "sWidth": "5%", "mRender": function (data, type, row) {
                    return "<input type='checkbox' class='checkboxes' value='" + data + "'/>";
                }},

                {
                    "mData": "imageUrl",
                    "sWidth": "95%"
                }
            ]
        });
    },

    /**
     * 获取行程信息
     */
    loadRouteInfo:function(){
        var me = this,
            formview = $("#Page_RouteFormView"),
            form = $("#RouteForm", formview),
            dt = $("#Page_RouteListView #datatable_route");

        dt.edit(function(id){
            $.Request.get("/rest/oms/route/" + id, null, function(result){
                $.Page.show("Page_RouteFormView", function(){
                    form.deserialize(result);
                });
            })
        })
    },

    /**
     * 保存行程基本信息
     */
    saveRouteInfo:function(){
        var me = this,
            formview = $("#Page_RouteFormView"),
            form = $('#RouteForm', formview),
            formdata = new FormData() ;

        formdata.append("route", JSON.stringify(form.serialize()));

        var images = $("input[type='file']");
        $.each(images, function(index, image){
            var file = $(image)[0].files[0];
            if(file) {
                formdata.append("imageUrl", file);
            }
        })

        $.Request.postFormData("/rest/oms/route/save",formdata,function(result){
            bootbox.alert("保存成功。", function(){
                "use strict";
                $.Page.back(function(){
                    me.query();
                });
            });
        })
    },

    /**
     * 装载行程安排信息
     */
    loadRouteScheduleInfo:function(){
        var view = $('#Page_ScheduleFormView');
        $("#img_scale").show();
        $.Page.show("Page_ScheduleFormView",function(){
            var map = new BMap.Map('map-container');//指向map的容器
            map.enableScrollWheelZoom(true);
            map.centerAndZoom('上海', 11);
        });
    },

    setScheduleWindow:function(){
        var view = $('#Page_ScheduleFormView'),
            sizeInput = $("#size", view),
            size = $("#size", view).val();

        if(sizeInput.val() == "full"){
            $("#page-title").hide();
            $("#page-bar").hide();

            sizeInput.val("restore");
        }else{
            $("#page-title").show();
            $("#page-bar").show();

            sizeInput.val("full");
        }
    }
};

