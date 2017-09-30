<#include "/macro/base.ftl" />

<form id="pagerForm" method="post" action="${base}/message/list">
	<@pagerForm />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${base}/message/list" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>消息ID： <input type="text" name="messageId"  />
					</td>

					<td>状态： <select name="status" id="status">
							<option value="">请选择</option>
							<#list messageStatus as sta>
    								<option value="${sta.name}" >${sta.desc}</option>
  							</#list>
					</select>
					</td>

					<td>消费队列： <select name="consumerQueue">
							<option value="">请选择</option>
					</select>
					</td>
					<td>
						<div class="subBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent"><button type="submit">查询</button></div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="reset">清空重输</button></div></div></li>
				</ul>
			</div>

					</td>
				</tr>
			</table>
			
		</div>
	</form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li class="line">line</li>
			<li><a class="delete" href="${base}/message/sendMessage/{sid}" target="ajaxTodo" title="确定要发送吗？" warn="请选中要发送的消息"><span>发送</span></a></li>
			<li class="line">line</li>
			<li><a class="edit" onclick="js_method(this)" target="ajaxTodo"  title="确定要发送所有死亡消息吗？"  id="sendAll" ><span>发送所有死亡消息</span></a></li>
			<li class="line">line</li>
		</ul>
	</div>

	<div id="w_list_print">
		<table class="list" width="100%" targetType="navTab" asc="asc" desc="desc"  layoutH="112">
			<thead>
				<tr>
					<th align="center" width="50">序号</th>
					<th>创建时间</th>
					<th>修改时间</th>
					<th>消息ID</th>
					<th>消息队列</th>
					<th>状态</th>
					<th width="60">重发次数</th>
					<th width="60">是否死亡</th>
				</tr>
			</thead>
			<tbody>
				<#if (page)??>
					<#list page.list as bean>
						<tr target="sid" rel="${bean.messageId}">
							<td align="center">${bean_index+1}</td>
							<td>${bean.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>${bean.editTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>${bean.messageId}</td>
							<td>${bean.consumerQueue}</td>
							<td>${bean.status}</td>
							<td>${bean.messageSendTimes}</td>
							<td>${bean.areadlyDead}</td>
						</tr>
					</#list>
				<#else>
					<tr>
						<td colspan="7" align="center">暂没记录</td>
					</tr>
				</#if>
			</tbody>
		</table>
	</div>
	<@pages />
</div>

<script type="text/javascript">
	function js_method(_a) {
		var queueName = document.getElementById("status").value;
		var url = "${base}/message/sendAllMessage?queueName=" + queueName;
		document.getElementById("sendAll").setAttribute("href", url);
		_a.click();
	}
</script>
