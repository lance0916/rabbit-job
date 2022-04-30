<#-- 自定义变量 -->
<#assign contextPath="${springMacroRequestContext.contextPath}" >

<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <title>蜗牛任务调度中心</title>
    <#import "common/common.ftl" as netCommon />
    <@netCommon.commonHead />
    <@netCommon.commonStyle />
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <#-- 头部区域 -->
    <@netCommon.commonHeader />

    <#-- 左侧菜单栏 -->
    <@netCommon.commonLeft "log" />

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">

            <fieldset class="layui-elem-field layui-field-title">
                <legend>日志管理</legend>
            </fieldset>

            <#-- 搜索 -->
            <form class="layui-form layui-form-pane">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">分组</label>
                        <div class="layui-input-block">
                            <select id="searchAppNameID" name="groupName" autocomplete="off"
                                    lay-filter="searchAppNameFilter">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">任务</label>
                        <div class="layui-input-block">
                            <select id="searchJobID" name="jobId" autocomplete="off">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">调度结果</label>
                        <div class="layui-input-block">
                            <select name="triggerCode" autocomplete="off">
                                <option value="">请选择</option>
                                <option value="200">成功</option>
                                <option value="500">失败</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">执行结果</label>
                        <div class="layui-input-block">
                            <select name="execCode" autocomplete="off">
                                <option value="">请选择</option>
                                <option value="200">成功</option>
                                <option value="500">失败</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <button lay-submit class="layui-btn" lay-filter="searchBtn">搜索</button>
                        <button type="reset" class="layui-btn layui-btn-primary">重置</button>
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

<#-- 日志详情弹窗 -->
<div id="logDetailLayer" class="layui-row" style="display:none;margin-right: 20px;">
    <div class="layui-col-lg12">
        <form id="logDetailFormId" class="layui-form" pane style="margin-top: 20px;" lay-filter="logDetailForm">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">执行地址</label>
                <div class="layui-input-block">
                    <input class="layui-text" name="execAddress" disabled/>
                </div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">执行参数</label>
                <div class="layui-input-block">
                    <textarea class="layui-textarea" name="execParam" disabled></textarea>
                </div>
            </div>
        </form>
    </div>
</div>

<#-- 公共 JS -->
<@netCommon.commonScript />

<#-- 单行操作按钮 -->
<script type="text/html" id="operateTool">
    <button type="button" class="layui-btn layui-btn-sm" lay-event="detail">详情</button>
    <button type="button" class="layui-btn layui-btn-sm" lay-event="log">执行日志</button>
</script>

<#-- 转义表格字段 -->
<script type="text/html" id="triggerCodeTpl">
    {{# if(d.trigger_code == 0){ }}
    <span style="color: #009688;">未调度</span>
    {{# } else if(d.trigger_code == 200){ }}
    <span style="color: dodgerblue;">成功</span>
    {{# } else if(d.trigger_code == 500){ }}
    <span style="color: red;">失败</span>
    {{# } else { }}
    <span style="color: #009688;">未知</span>
    {{# } }}
</script>
<script type="text/html" id="execCodeTpl">
    {{# if(d.exec_code == 0){ }}
    <span style="color: #009688;">未执行</span>
    {{# } else if(d.exec_code == 200){ }}
    <span style="color: dodgerblue;">成功</span>
    {{# } else if(d.exec_code == 500){ }}
    <span style="color: red;">失败</span>
    {{# } else { }}
    <span style="color: #009688;">未知</span>
    {{# } }}
</script>

<script>
    let element = layui.element;
    let layer = layui.layer;
    let table = layui.table;
    let $ = layui.jquery;
    let form = layui.form;
    let laydate = layui.laydate;

    !function () {
        // 渲染表格
        table.render({
            elem: '#dataTableID',
            url: '${contextPath}/log',
            cols: [[
                {type: 'numbers'},
                {field: 'jobName', title: '任务名称'},
                {field: 'execAddress', title: '执行地址'},
                {field: 'execHandler', title: 'JobHandler'},
                {field: 'triggerTime', title: '调度时间', minWidth: 165},
                {field: 'triggerCode', title: '调度结果', templet: '#triggerCodeTpl'},
                {field: 'execBeginTime', title: '执行时间', minWidth: 165},
                {field: 'execCode', title: '执行结果', templet: '#execCodeTpl'},
                {fixed: 'right', title: '操作', minWidth: 145, toolbar: '#operateTool'},
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
            let htmlContent = "<option value=''>请选择</option>";
            contentArray.forEach(function (item) {
                htmlContent += "<option value='" + item.name + "'>" + item.title + "</option>";
            })
            // 渲染查询条件的分组
            $('#searchAppNameID option').remove();
            $('#searchAppNameID').append(htmlContent);

            // 渲染清理日志的分组
            $('#delAppNameID option').remove();
            $('#delAppNameID').append(htmlContent);
            form.render();
        });

        // 渲染时间组件
        laydate.render({
            elem: '#timeRangeID',
            type: 'date',
            range: true,
            format: 'yyyy-MM-dd HH:mm:ss'
        })
    }();

    table.on('tool(dataTable)', function (obj) {
        let data = obj.data;
        if (obj.event === 'detail') {
            form.val('logDetailForm', data);
            layer.open({
                type: 1,
                title: '详情',
                area: ['600px', '250px'],
                offset: '50px',
                resize: false,
                content: $('#logDetailLayer')
            })
        } else if (obj.event === 'log') {
            layer.msg('查看日志')
        }
    });

    // 监听执行器的变更
    form.on('select(searchAppNameFilter)', onAppNameChange);
    form.on('select(delAppNameFilter)', onAppNameChange);

    function onAppNameChange(data) {
        let $elem = $(data.elem);
        let $this = undefined;
        if ($elem.attr("id") === 'searchAppNameID') {
            $this = $('#searchJobID');
        } else if ($elem.attr("id") === 'delAppNameID') {
            $this = $('#delJobID');
        } else {
            layer.alert('内部错误');
            return false;
        }
        if (data.value === '') {
            $this.find('option').remove();
            $this.append("<option value=''>请选择</option>");
            form.render();
            return;
        }
        // 渲染路由策略
        $.get('${contextPath}/job/listByAppName', {appName: data.value}, function (ret) {
            if (ret.code !== 200) {
                layer.error('渲染路由策略异常');
                return;
            }
            let contentArray = ret.content;
            let htmlContent = "<option value=''>请选择</option>";
            contentArray.forEach(function (item) {
                htmlContent += "<option value='" + item.id + "'>" + item.name + "</option>";
            })
            $this.children().remove();
            $this.append(htmlContent);
            form.render();
        });
    }

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

</script>
</body>
</html>