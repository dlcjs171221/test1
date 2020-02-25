<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	String cPage = request.getParameter("cPage");
	String b_idx = request.getParameter("b_idx");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/summernote-lite.css">
<style type="text/css">
	#bbs table {
	    width:580px;
	    margin-left:10px;
	    border:1px solid black;
	    border-collapse:collapse;
	    font-size:14px;
	    
	}
	
	#bbs table caption {
	    font-size:20px;
	    font-weight:bold;
	    margin-bottom:10px;
	}
	
	#bbs table th {
	    text-align:center;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	#bbs table td {
	    text-align:left;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	.no {width:15%}
	.subject {width:30%}
	.writer {width:20%}
	.reg {width:20%}
	.hit {width:15%}
	.title{background:lightsteelblue}
	
	.odd {background:silver}
	

		
</style>

</head>
<body>
	<div id="bbs">
	<form action="control?type=write" method="post" encType="multipart/form-data">
		<table summary="게시판 글쓰기">
			<caption>게시판 글쓰기</caption>
			<tbody>
				<tr>
					<th>제목:</th>
					<td><input type="text" name="title" size="45"/></td>
				</tr>
				<tr>
					<th>이름:</th>
					<td><input type="text" name="writer" size="12"/></td>
				</tr>
<!-- 				<tr>
					<th>내용:</th>
					<td><textarea id="content" name="content" cols="50" 
							rows="8"></textarea></td>
				</tr>
-->
				<tr>
					<th>첨부파일:</th>
					<td><input type="file" name="file"/></td>
				</tr>
<!--
				<tr>
					<th>비밀번호:</th>
					<td><input type="password" name="pwd" size="12"/></td>
				</tr>
-->
<!--				<tr>
					<td colspan="2" >
						<input type="button" value="보내기"
						onclick="sendData()"/>
						<input type="button" value="다시"/>
						<input type="button" value="목록"/>
					</td>
				</tr>
-->
			</tbody>
		</table>
		
		<input type="hidden" name="content" id="str" />
		
	</form>
	
		<table>
			<tbody>
				<tr>
					<th style="width:83px;">내용:</th>
					<td><textarea id="content" name="content" cols="50" 
							rows="8"></textarea></td>
				</tr>
				<tr>
					<td colspan="2" >
						<input type="button" value="보내기"
						onclick="sendData()"/>
						<input type="reset" value="초기화"/>
						<input type="button" value="목록보기" onclick="javascript:location.href='control?type=list&cPage=<%=cPage%>&b_idx=<%=b_idx %>'"/>
					</td>
				</tr>
			</tbody>
		</table>
		
	</div>
		
	<script src="js/jquery-3.4.1.min.js"></script>
	<script src="js/summernote-lite.js"></script>
	<script src="js/lang/summernote-ko-KR.min.js"></script>
	
	<script>

	function sendData(){
		
		for(var i=0 ; i<document.forms[0].elements.length ; i++){
			
			//만약 제목과 이름만 입력되었는지 유효성 검사를 한다면.......
			if( i > 1)
				break;
			
			if(document.forms[0].elements[i].value == ""){
				console.log(document.forms[0].elements[i]);
				//alert(document.forms[0].elements[i].name+ "를 입력하세요");
				document.forms[0].elements[i].focus();
				return;//수행 중단
			}
		}
		var str = $("#content").val();
		//console.log(str);
		$("#str").val(str);
		
		//document.forms[0].action = "test.jsp";
		document.forms[0].submit();
	}	
	
	$(function() {
		$("#content").summernote({
			height: 300, width: 500, lang:"ko-KR", 
			callbacks:{ 
				onImageUpload: function(files, editor){
					//이미지가 에디터에 추가 될때마다 수행하는 곳
					//console.log("ttttttttt");
					//이미지를 첨부하면 배열로 인식된다.
					//이것을 서버로 비동기식통신을 수행하는 함수를 호출하여 업로드시킨다.
					for(var i=0; i<files.length; i++){
						sendFile(files[i],editor);
					}
				} 
			} 
		});
		
		$("#content").summernote("lineHeight",1.0);
	});
	function sendFile(file,editor){
		//이미지를 서버로 업로드 시키기위해 비동기식 통신을 수행하자
		
		//파라미터를 전달하기위해 폼객체 준비.
		var frm = new FormData(); //<form encType='multipart/form-data'></form>
		
		//보내고자하는 자원을 파라미터 값으로 등록(추가)
		frm.append("upload",file);
		
		//비동기식 통신
		$.ajax({
			url: "control?type=saveImage",	//요청할 URL
			type: "post",		//get, post 중 전송방식을 선택한다.
			dataType: "json",	//서버에서 받을 데이터 형식을 지정한다.	지정하지 않으면 MIME 타입을 참고하여 자동 파싱된다.
			
			// 파일을 보낼 때는 일반적인 데이터 전송이 아님을 증명해야 한다.
			
			contentType: false, //해더의 Content-Type을 설정한다.

			processData: false, //데이터를 querystring 형태로 보내지 않고 DOMDocument 또는 다른 형태로 보내고 싶으면 false로 설정한다.

			//data: "v1="+encodeURIComponent(값)
			data: frm //서버로 보낼 데이터
			
		}).done(function(data){
			//console.log(data.img_url);
			//에디터에 img태그로 저장하기위해 img태그를 만들고 src 속성을 작성해야함
			//var img = $("<img>").attr("src",data.img_url);
			
			//$("#content").summernote("insertNode",img[0]);
			
			$("#content").summernote("editor.insertImage",data.url);
			
			//console.log(data.str);
			
		}).fail(function(err){
			console.log(err);
		});
	}
</script>
</body>
</html>












