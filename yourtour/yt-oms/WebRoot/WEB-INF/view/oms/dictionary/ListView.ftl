<div id="Page_DictListView" class="row" data-role="page">
    <div class="col-lg-12">
        <div class="page-bar">
            <ul class="page-breadcrumb">
                <li>
                    <i class="icon-home"></i>
                    <a href="index.html">首页</a>
                    <i class="fa fa-angle-right"></i>
                </li>
                <li>
                    <span>基础数据管理</span>
                </li>
            </ul>
        </div>
        <!-- END PAGE HEADER-->

        <div class="row">
            <div class="col-md-12">
                <!-- BEGIN SAMPLE FORM PORTLET-->
                <div class="portlet light ">
                    <div class="portlet-body">
                        <div id="DictListView_Criteria">
                            <div class="row bottom-space">
                                <div class="col-md-6 col-sm-12">
                                    <div class="table-group-actions pull-left">
                                        <select id="type" name="type" class="table-group-action-input form-control input-inline">
                                            <#list types as type>
                                                <option value="${type.code}">${type.name}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </div>

                                <div class="col-md-6 col-sm-12">
                                    <div class="pull-right">
                                        <button id="btn_add" class="btn red">
                                            <i class="fa fa-plus"></i> 新增</button>

                                        <button id="btn_edit" class="btn default">
                                            <i class="fa fa-edit"></i> 修改</button>

                                        <button id="btn_delete" class="btn default">
                                            <i class="fa fa-times"></i> 删除</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <table id="datatable_dict" class="row-border" cellspacing="0" width="100%" data-criteria="DictListView_Criteria"
                               data-rest="${context}/rest/oms/dicts/query">
                            <thead>
                                <tr>
                                    <th>
                                        <input type="checkbox" class="group-checkable">
                                    </th>
                                    <th>分类</th>
                                    <th>名称</th>
                                    <th>编码</th>
                                    <th>值</th>
                                </tr>
                            </thead>

                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
