package Bbs.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mybatis.dao.BbsDAO;

public class DeleteAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
				 
			//파라미터 받기
			String b_idx = request.getParameter("b_idx");
		 	String cpage = request.getParameter("cPage");
		 	String pw = request.getParameter("pw");
			
		 	System.out.println(pw);
			boolean chk = BbsDAO.del(b_idx, pw);
						
			return "/control";

	}
}
