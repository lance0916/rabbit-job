<#-- 自定义变量 -->
<#assign contextPath="${springMacroRequestContext.contextPath}" >

<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <title>蜗牛任务调度中心</title>
    <#import "common.ftl" as netCommon />
    <@netCommon.commonHead />
    <@netCommon.commonStyle />
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <#-- 头部区域 -->
    <@netCommon.commonHeader />

    <#-- 左侧菜单栏 -->
    <@netCommon.commonLeft "info" />

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">

            <fieldset class="layui-elem-field layui-field-title">
                <legend>任务管理</legend>
            </fieldset>

            <#-- 搜索 -->
            <form class="layui-form layui-form-pane">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <button id="addBtn" type="button" class="layui-btn layui-btn-normal">新增</button>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">分组</label>
                        <div class="layui-input-block">
                            <select id="searchGroupNameID" name="groupName" autocomplete="off">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">名称</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" class="layui-input" autocomplete="off"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">负责人</label>
                        <div class="layui-input-block">
                            <input type="text" name="author" class="layui-input" autocomplete="off"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <button lay-submit class="layui-btn" lay-filter="searchBtn">搜索</button>
                    </div>
                </div>
            </form>

            <#-- 数据表格 -->
            <table class="layui-hide" id="dataTableID" lay-filter="dataTable"></table>

        </div>
    </div>

    <!-- 底部固定区域 -->
    <@netCommon.commonFooter />
</div>

<#-- 编辑 -->
<div id="editLayer" class="layui-row" style="display:none;margin-right: 20px;">
    <div class="layui-col-lg12">
        <form id="editFormID" class="layui-form layui-form-pane" style="margin-top: 20px; margin-left: 20px"
              lay-filter="editForm">
            <div class="layui-form-item layui-hide">
                <div class="layui-input-inline">
                    <input class="layui-input" name="id" value=""/>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">任务名称</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="name" required lay-verify="required" autocomplete="off"/>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">Cron</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="cron" required lay-verify="required" autocomplete="off"/>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">路由策略</label>
                    <div class="layui-input-inline">
                        <select id="editExecutorRouteStrategyID" name="execRouteStrategy" required
                                lay-verify="required">
                            <option value="">请选择</option>
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">任务分组</label>
                    <div class="layui-input-inline">
                        <select id="editGroupNameID" name="appName" required lay-verify="required">
                            <option value="">请选择</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">负责人</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="authorName" required lay-verify="required" autocomplete="off"/>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">报警邮箱</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="authorEmail" autocomplete="off"/>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">超时时间</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="execTimeout" autocomplete="off"/>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">重试次数</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" name="execFailRetryCount" autocomplete="off"/>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">JobHandler</label>
                <div class="layui-input-inline">
                    <input class="layui-input" name="execHandler" required lay-verify="required"
                           autocomplete="off"/>
                </div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">执行参数</label>
                <div class="layui-input-block">
                    <textarea class="layui-textarea" name="execParam" placeholder=""></textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button lay-submit class="layui-btn" lay-filter="saveBtn">保存</button>
                </div>
            </div>
        </form>
    </div>
</div>

<div id="execLayer" class="layui-row" style="display:none;margin-right: 20px;">
    <div class="layui-col-lg12">
        <form id="execFormId" class="layui-form layui-form-pane" pane style="margin-top: 20px; margin-left: 20px"
              lay-filter="execForm">
            <input type="hidden" name="id" id="execId">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">执行参数</label>
                <div class="layui-input-block">
                    <textarea class="layui-textarea" name="exeParam" placeholder=""></textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button lay-submit class="layui-btn" lay-filter="execBtn">执行</button>
                </div>
            </div>
        </form>
    </div>
</div>

<#-- 公共 JS -->
<@netCommon.commonScript />

<script type="text/html" id="triggerStatusTpl">
    <input type="checkbox" lay-skin="switch" lay-text="运行中|已停止" disabled
           {{# if(d.triggerStatus== 1){ }} checked {{# } }}
    >
</script>

<script type="text/html" id="tableToolTpl">
    <button class="layui-btn layui-btn-sm" lay-event="edit">编辑</button>
    <a class="layui-btn layui-btn-sm" lay-event="more">更多 <i class="layui-icon layui-icon-down"></i></a>
</script>

<script>
    let element = layui.element;
    let layer = layui.layer;
    let table = layui.table;
    let $ = layui.jquery;
    let form = layui.form;
    var dropdown = layui.dropdown;

    !function () {
        // 渲染表格
        table.render({
            elem: '#dataTableID',
            url: '${contextPath}/job',
            defaultToolbar: [],
            cols: [[
                {field: 'id', width: 50, title: 'ID'},
                {field: 'name', title: '任务名'},
                {field: 'cron', title: 'CRON'},
                {field: 'execHandler', title: '执行方法名'},
                {field: 'authorName', title: '负责人'},
                {field: 'triggerStatus', title: '状态', templet: '#triggerStatusTpl'},
                {fixed: 'right', width: 160, title: '操作', templet: '#tableToolTpl'},
            ]],
            page: true,
            response: {
                statusCode: 200
            },
            parseData: function (res) {
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.content.total,
                    "data": res.content.records
                };
            }
        });

        // 渲染分组下拉框
        $.get('${contextPath}/app/listAll', function (ret) {
            if (ret.code !== 200) {
                layer.error('渲染任务分组异常');
                return;
            }
            let contentArray = ret.content;
            let htmlContentSearch = "<option value=''>请选择</option>";
            let htmlContentEdit = "<option value=''>请选择</option>";
            contentArray.forEach(function (item) {
                htmlContentSearch += "<option value='" + item.name + "'>" + item.title + "</option>";
                htmlContentEdit += "<option value='" + item.name + "'>" + item.title + "</option>";
            })
            $('#searchGroupNameID option').remove();
            $('#searchGroupNameID').append(htmlContentSearch);
            $('#editGroupNameID option').remove();
            $('#editGroupNameID').append(htmlContentEdit);
            form.render();
        });

        // 渲染路由策略
        $.get('${contextPath}/job/route', function (ret) {
            if (ret.code !== 200) {
                layer.error('渲染路由策略异常');
                return;
            }
            let contentArray = ret.content;
            let htmlContent = "<option value=''>请选择</option>";
            contentArray.forEach(function (item) {
                htmlContent += "<option value='" + item.name + "'>" + item.desc + "</option>";
            })
            $('#editExecutorRouteStrategyID option').remove();
            $('#editExecutorRouteStrategyID').append(htmlContent);
            form.render();
        });
    }();

    // 操作事件
    table.on('tool(dataTable)', function (obj) {
        var that = this, tdata = obj.data;
        if (obj.event === 'edit') {
            form.val('editForm', tdata);
            form.render('radio');
            layer.open({
                type: 1,
                title: '新增任务',
                area: ['700px', '570px'],
                offset: '50px',
                content: $('#editLayer')
            })
        } else if (obj.event === 'more') {
            //更多下拉菜单
            dropdown.render({
                elem: that,
                show: true,
                data: [{
                    title: '执行一次',
                    id: 'exec'
                }, {
                    title: '启动',
                    id: 'start'
                }, {
                    title: '停止',
                    id: 'stop'
                }, {
                    title: '下次执行时间',
                    id: 'nextTriggerTime'
                }, {
                    title: '查询日志',
                    id: 'log'
                }, {
                    title: '删除',
                    id: 'del'
                }],
                click: function (data, othis) {
                    // 根据 id 做出不同操作
                    if (data.id === 'exec') {
                        $("#execId").val(tdata.id)
                        layer.open({
                            type: 1,
                            title: '执行任务',
                            area: ['600px', '300px'],
                            offset: '50px',
                            resize: false,
                            content: $('#execLayer')
                        })
                    } else if (data.id === 'start') {
                        if (tdata.triggerStatus === 0) {
                            $.post('${contextPath}/job/start/' + tdata.id, function (ret) {
                                if (ret.code === 200) {
                                    layer.alert('启动成功');
                                    table.reload('dataTableID');
                                } else {
                                    layer.alert(ret.msg);
                                }
                            });
                        } else {
                            layer.alert('任务已启动');
                        }
                    } else if (data.id === 'stop') {
                        if (tdata.triggerStatus === 1) {
                            $.post('${contextPath}/job/stop/' + tdata.id, function (ret) {
                                if (ret.code === 200) {
                                    layer.alert('停止成功');
                                    table.reload('dataTableID');
                                } else {
                                    layer.alert(ret.msg);
                                }
                            });
                        } else {
                            layer.alert('任务已停止');
                        }
                    } else if (data.id === 'nextTriggerTime') {
                        let cron = tdata.cron;
                        $.get('${contextPath}/job/nextTriggerTime', {cron: cron}, function (ret) {
                            if (ret.code !== 200) {
                                layer.alert(ret.msg);
                                return;
                            }
                            let htmlContent = '<div>';
                            let execTimeArray = ret.content;
                            for (let i = 0; i < execTimeArray.length; i++) {
                                htmlContent += (i + 1) + ': ' + execTimeArray[i];
                                htmlContent += '<br/>';
                            }
                            htmlContent += '</div>';
                            layer.open({
                                resize: false,
                                content: htmlContent
                            });
                        });
                    } else if (data.id === 'log') {
                        layer.msg('查询日志');
                    } else if (data.id === 'del') {
                        layer.confirm('是否删除该条记录?', {icon: 3, title: '提示'}, function (index) {
                            layer.close(index);
                            $.ajax({
                                url: '${contextPath}/job/' + tdata.id,
                                type: 'post',
                                data: {_method: 'delete'},
                                success: function (ret) {
                                    if (ret.code === 200) {
                                        table.reload('dataTableID');
                                    } else {
                                        layer.alert(ret.msg);
                                    }
                                }
                            })
                        });
                    }
                },
                align: 'right', //右对齐弹出
                style: 'box-shadow: 1px 1px 10px rgb(0 0 0 / 12%);'
            });
        }
    });

    // 添加弹窗
    $('#addBtn').click(function () {
        $('#editFormID')[0].reset();
        form.render('radio');

        layer.open({
            type: 1,
            title: '新增任务',
            area: ['700px', '570px'],
            offset: '50px',
            content: $('#editLayer')
        })
    })

    // 编辑表单提交
    form.on('submit(saveBtn)', function (data) {
        let field = data.field;
        let method = field.id === '' ? 'post' : 'put';
        $.ajax({
            url: '${contextPath}/job?_method=' + method,
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(field),
            success: function (ret) {
                if (ret.code !== 200) {
                    layer.alert(ret.msg);
                    return;
                }
                layer.closeAll('page');
                layer.alert("保存成功");

                // 刷新表格
                table.reload('dataTableID');
            }
        });
        return false;
    });

    // 搜索提交
    form.on('submit(searchBtn)', function (data) {
        table.reload('dataTableID', {
            where: data.field,
            page: {
                curr: 1
            }
        });
        return false;
    });

    // 执行一次
    form.on('submit(execBtn)', function (data) {
        $.post('${contextPath}/job/exec/' + data.field.id, {execParam: data.field.exeParam}, function (ret) {
            layer.closeAll('page');
        });
        return false;
    });

</script>
</body>
</html>