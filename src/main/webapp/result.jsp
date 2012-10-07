<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
	<link rel="stylesheet" href="css/jquery-ui-1.8.24.custom.css" type="text/css" />
	<script src="js/jquery-1.8.2.min.js" type="text/javascript"></script>
	<script src="js/jquery-ui-1.8.24.custom.min.js" type="text/javascript"></script>
	<title>Resulta'do da busca sacks-robot</title>
	<style>
		.preco{
			font-family: cursive;
			font: normal medium;
			color: red;
		}
		.descricao{
			font-family: fantasy sans-serif serif;
			font-size: 12px;
		}
	</style>
</head>
<body>
	<table class="ui-widget ui-widget-content">
		<c:set var="count" value="0" />
		<c:forEach var="p" items="${requestScope.produtos}">
			<c:if test="${count % 3 == 0 }">
				<tr>
			</c:if>
				<td>
					<table class="ui-widget ui-widget-content">
						<tr>
							<td>
								<a href="${p.urlOferta}" target="_blank">
									<img src="${p.urlImagem}"/>
								</a>
							</td>
						</tr>
						<tr>
							<td><span class="preco">${p.precoPorStr}</span></td>
						</tr>
						<tr>
							<td><span class="descricao">${p.descricao}</span></td>
						</tr>
					</table>
				</td>
			<c:if test="${count+1 % 3 == 0 }">
				</tr>
			</c:if>
			<c:set var="count" value="${count +1 }" />
		</c:forEach>
	</table>

</body>
</html>