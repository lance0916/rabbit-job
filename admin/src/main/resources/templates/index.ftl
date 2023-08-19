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
    <@netCommon.commonLeft "index" />

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">

            <fieldset class="layui-elem-field layui-field-title">
                <legend>调度中心状态</legend>
            </fieldset>
            <table class="layui-table" lay-size="lg">
                <tbody>
                <tr>
                    <td style="width: 120px;font-size: large;font-weight: bold;">执行器数量</td>
                    <td>
                        <span id="appNum"></span>
                    </td>
                    <td style="width: 100px;font-size: large;font-weight: bold;">任务数量</td>
                    <td>
                        <span id="jobNum"></span>
                    </td>
                    <td style="width: 120px;font-size: large;font-weight: bold;">累积调度次数</td>
                    <td>
                        <span id="triggerNum"></span>
                    </td>
                    <td style="width: 120px;font-size: large;font-weight: bold;">成功次数</td>
                    <td>
                        <span id="triggerSuccessNum"></span>
                    </td>
                    <td style="width: 120px;font-size: large;font-weight: bold;">失败次数</td>
                    <td>
                        <span id="triggerFailNum"></span>
                    </td>
                </tr>
                </tbody>
            </table>

            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                <legend>今日调度情况</legend>
            </fieldset>
            <table class="layui-table" lay-size="lg">
                <tbody>
                <tr>
                    <td style="width: 100px;font-size: large;font-weight: bold;">调度次数</td>
                    <td>
                        <span id="todayTriggerNum"></span>
                    </td>
                    <td style="width: 100px;font-size: large;font-weight: bold;">成功次数</td>
                    <td>
                        <span id="todayTriggerSuccessNum"></span>
                    </td>
                    <td style="width: 100px;font-size: large;font-weight: bold;">失败次数</td>
                    <td>
                        <span id="todayTriggerFailNum"></span>
                    </td>
                </tr>
                </tbody>
            </table>

            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                <legend>系统情况</legend>
            </fieldset>
            <div class="layui-bg-gray" style="padding: 30px;">
                <div class="layui-row layui-col-space15">
                    <div class="layui-col-md4">
                        <div class="layui-card">
                            <div class="layui-card-header">
                                CPU信息
                            </div>
                            <div id="cpuInfo" class="layui-card-body">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md4">
                        <div class="layui-card">
                            <div class="layui-card-header">
                                内存信息
                            </div>
                            <div id="memoryInfo" class="layui-card-body">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md4">
                        <div class="layui-card">
                            <div class="layui-card-header">
                                Java信息
                            </div>
                            <div id="javaInfo" class="layui-card-body">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <!-- 底部固定区域 -->
    <@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
<script>
    let element = layui.element;
    let $ = layui.jquery;

    !function () {
        $.get('${contextPath}/system/status/trigger', function (ret) {
            if (ret.code !== 200) {
                layer.error(ret.msg);
                return;
            }
            let data = ret.content;
            $("#appNum").html(data.appNum);
            $("#jobNum").html(data.jobNum);
            $("#triggerNum").html(data.triggerNum);
            $("#triggerSuccessNum").html(data.triggerSuccessNum);
            $("#triggerFailNum").html(data.triggerFailNum);
            $("#todayTriggerNum").html(data.todayTriggerNum);
            $("#todayTriggerSuccessNum").html(data.todayTriggerSuccessNum);
            $("#todayTriggerFailNum").html(data.todayTriggerFailNum);
        });

        $.get('${contextPath}/system/status/cpu', function (ret) {
            if (ret.code !== 200) {
                layer.error(ret.msg);
                return;
            }
            $("#cpuInfo").html(ret.content);
        });

        $.get('${contextPath}/system/status/memory', function (ret) {
            if (ret.code !== 200) {
                layer.error(ret.msg);
                return;
            }
            $("#memoryInfo").html(ret.content);
        });

        $.get('${contextPath}/system/status/java', function (ret) {
            if (ret.code !== 200) {
                layer.error(ret.msg);
                return;
            }
            $("#javaInfo").html(ret.content);
        });

    }();

</script>
</body>
</html>