package Bbs.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;

public class ViewAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		
		//1. request에 저장된 변수를 받는다.
		
		String cPage = request.getParameter("cPage");
		String b_idx = request.getParameter("b_idx");
		
		
		//2. DAO 로 인자전달
		BbsVO vo = BbsDAO.getView(b_idx);
	
		//3.
		request.setAttribute("vo",vo);
		
		
		HttpSession session = request.getSession();//*****************session 얻기
		
		Object obj = session.getAttribute("read_bbs"); //조회수 
		
		if(vo != null){	
			
			boolean chk1 = false;
			List<BbsVO> list = null;
			
		
			if(obj == null){
				list = new ArrayList<BbsVO>();
				session.setAttribute("read_bbs", list);
			}else{
				list = (List<BbsVO>)obj;
				
				//vo의 b_idx와 list의 있는 각 BbsVO의 b_idx를 비교 
				for(BbsVO r_vo : list){
					if(b_idx.equals(r_vo.getB_idx())){
						chk1 = true;
						break;  
					}
				}
			}
			
			//String idx = (String)session.getAttribute(b_idx);
			
			if(!chk1){ //chk1 == null
			
				boolean chk = BbsDAO.hit(b_idx);
						
			}
			
		}
		
		return "/view.jsp";
		
	}

}