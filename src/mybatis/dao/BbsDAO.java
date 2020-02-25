package mybatis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import mybatis.service.FactoryService;
import mybatis.vo.BbsVO;

public class BbsDAO {

	//전체게시물의 수를 반환하는 기능 - list.jsp에서 호출
	public static int getTotalcount() {
				
		SqlSession ss = FactoryService.getFactory().openSession();
		
		int cnt = ss.selectOne("bbs.totalCount");
		
		ss.close();
		
		return cnt;
		
	}
	
	//원하는 페이지의 게시물들을 목록화면으로 표현하기위해 배열로 반환하는 기능 - list.jsp
	public static BbsVO[] getList(int begin, int end) {
		
		BbsVO[] ar = null;
		
		SqlSession ss = FactoryService.getFactory().openSession();
		
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("begin",String.valueOf(begin));
		map.put("end",String.valueOf(end));
		
		//list라는 맵퍼호출
		List<BbsVO> list = ss.selectList("bbs.bbsList",map);
		//각 게시물을 의미하는 객체가 BbsVO이다. 그안에 댓글들이 들어온상태
		
		//받은 리스트를 준비된 배열로 변환한다.
		if( list != null) {
			ar = new BbsVO[list.size()];

			//받은 list구조를 배열로 변환
			list.toArray(ar);
		}
					
		ss.close();
		
		return ar;
		
		//String.valueOf() - 숫자->문자열
	}
	
	
	//b_idx값을 인자로 받아서 특정 게시물을 반환하는 기능 - view.jsp
	public static BbsVO getView(String b_idx) {
		
		SqlSession ss = FactoryService.getFactory().openSession();
			
		BbsVO vo = ss.selectOne("bbs.view",b_idx);
			
		ss.close();
			
		return vo;
	}
	
	
	
	
	//원글 저장기능
	public static void writeAdd(String title, String writer, String content, String file_name, String ip, String ori_name){
			
			String pwd = "1111";
		
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", title);
			map.put("writer", writer);
			map.put("content", content);
			map.put("file", file_name);
			map.put("ori_name", ori_name);
			map.put("ip", ip);
			map.put("pwd", pwd);
			
			SqlSession ss = FactoryService.getFactory().openSession();
					
			int cnt = ss.insert("bbs.add", map);	//추가에 성공하면 cnt에는 1이 저장된다.

			if(cnt>0) {
				ss.commit();
			}
			ss.close();
	}
	
	
	// 댓글 저장기능
	public static void commAdd(String writer, String content, String pwd,String ip,String b_idx){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("writer", writer);
		map.put("content", content);
		map.put("ip", ip);
		map.put("pwd", pwd);
		map.put("b_idx", b_idx);
		
		SqlSession ss = FactoryService.getFactory().openSession();
				
		int cnt = ss.insert("bbs.commAdd", map);	//추가에 성공하면 cnt에는 1이 저장된다.

		if(cnt>0) {
			ss.commit();
		}else {
			ss.rollback();
		}
		ss.close();
		
	}
	
	
	//원글 수정기능
	public static boolean edit(String b_idx, String title,String writer,String content) {
			boolean chk = false;

			SqlSession ss = FactoryService.getFactory().openSession();

			Map<String, String> map = new HashMap<String, String>();
			map.put("title", title);
			map.put("writer", writer);
			map.put("content", content);
			map.put("b_idx", b_idx);

			int cnt = ss.update("bbs.edit", map);

			if(cnt > 0) {
				chk = true;
				ss.commit();
			}

			ss.close();

			return chk;
		}
		
	
	//인자로 삭제할 원글의 b_idx와 pw를 받아 처리하는 기능
		public static boolean del(String b_idx, String pw) {
			
			boolean chk = false;
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("b_idx", b_idx);
			map.put("pw", pw);
			
			System.out.println(pw);
			SqlSession ss = FactoryService.getFactory().openSession();
			
			int cnt = ss.update("bbs.del",map);
			
			if(cnt > 0) {
				ss.commit();
				chk = true;
			}else
				ss.rollback();
			
			ss.close();
			
			return chk;
		}
			

	//조회수 올리기
	public static boolean hit(String b_idx) {
		
		boolean chk = false;
		
		SqlSession ss = FactoryService.getFactory().openSession();
		int cnt = ss.update("bbs.hit",b_idx);
		
		if(cnt > 0) {
			chk = true;
			ss.commit();
		}
				
		ss.close();
		
		return chk;
	}
	
}
