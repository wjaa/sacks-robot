<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/jquery-ui-1.8.24.custom.css" type="text/css" />
<script src="js/jquery-1.8.2.min.js" type="text/javascript"></script>
<script src="js/jquery-ui-1.8.24.custom.min.js" type="text/javascript"></script>
<script type="text/javascript" src="http://ajax.microsoft.com/ajax/jquery.validate/1.6/jquery.validate.js" ></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form id="formMain" action="main">
	  	<div style="width:100%">
		  	<table class="ui-widget ui-widget-content" style="width:100%">
		  		<tr>
		  			<td width="300px">
		  				<table>
		  					<tr>
		  						<td>Palavras chaves:</td>
		  						<td><input type="text" name="keyword"></td>
		  					</tr>
		  					<tr>
		  						<td>Valor:</td>
		  						<td><input type="text" name="valor"></td>
		  					</tr>
		  				</table>
		  			</td>
		  			<td align="left">
		  				<div style="margin: 10px;">
		  					<input type="radio"  name="filtertype" value="0">Maior que<br>
							<input type="radio" name="filtertype" value="1"checked > Menor que<br>
							<input type="radio" name="filtertype" value="2" >Igual a<br>
						</div>
					</td>
					<td align="left">
		  				<div style="margin: 10px;">
		  					<input type="radio"  name="ordertype" value="0">Maior preço<br>
							<input type="radio" name="ordertype" value="1"checked >Menor preço<br>
						</div>
					</td>

		  		<tr>
		  			<td colspan="3" align="center">
		  				<input type="submit" value="Buscar">
		  				<input type="reset" value="Limpar">
		  			</td>
		  		</tr>
		  		
		  	</table>
	  	</div>
  	</form>
  	<div id="resultBusca" style="width:100%">
  		
  	</div>
  	<div id="dialogAguarde" style="width:100%; display:none;">
  		<img src="images/wait.gif">
  		Aguarde...
  	</div>
	 
  
</body>
</html>
<script>
	
	$(document).ready(function(){  
        $('#formMain').validate({
        	rules:{
	            valor: {
	                required: true,
	            },
	            filtertype: {
	                required: true
	            }

	        },
	        messages:{
	            keyword:{
	                required: "O campo 'palavra-chave' é obrigatorio.",
	                minlength: "O campo 'palavra-chave' deve conter no mínimo 3 caracteres."
	            },
	            valor: {
	                required: "O campo 'valor' é obrigatorio",
	            },
	            filtertype: {
	                required: "O campo 'tipo de filtro' é obrigatorio"
	            }
	            
	        },  
            submitHandler: function( form ){
        		$("#dialogAguarde").dialog({
        			height: 260,
        			modal: true
        		});	
                var dados = $( form ).serialize();  
  
                
                $.ajax({
                  type: "POST",
      			  url:"main",
      			  data:dados,
      			  success:function(data){
      				$("#dialogAguarde").dialog("destroy");			  
      				$("#resultBusca").html(data);
      		  	  },
      		  	  error:function(data){
      		  		$("#dialogAguarde").dialog("destroy");
      		  		$("#resultBusca").html("Erro na busca.");
      		  	  }

      			});
                
                return false;  
            }  
        });  
    });  
	
	
	
	
</script>