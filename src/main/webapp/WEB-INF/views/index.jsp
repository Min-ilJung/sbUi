<%@ page import="java.sql.Timestamp" %><%--
  Created by IntelliJ IDEA.
  User: USER
  Date: 2023-03-20
  Time: 오전 11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
Timestamp register =new Timestamp(System.currentTimeMillis());
%>
<html>
<head>
    <title>Index View</title>

    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <script type="text/javascript" src="/js/site-common.js?<%=register%>"></script>
    <script>
        $(function () {
            XE.get('/api/basic',{}, function(res){
                XE.resMessage(res, function (result) {
                    console.log(res);
                    console.log(result);
                    $("#userName").html(result.name);
                });
            });
        });
    </script>
</head>
<body>
    Index 화면 입니다.
    <br><br>
    이 파일에 기본 화면을 구현하시면 됩니다.

    <p>API 응답값: <span id="userName" class="xe-font-bold"></span></p>
</body>
</html>