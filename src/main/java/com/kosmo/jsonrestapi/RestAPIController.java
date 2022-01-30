package com.kosmo.jsonrestapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mybatis.BoardDTO;
import mybatis.IBoardDAO;
import mybatis.ParameterDTO;

@Controller
public class RestAPIController {

	//Mybaits 사용을 위한 자동주입
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping("/restapi/boardList.do")  //요청명에 대한 매핑
	@ResponseBody //컬렉션에 저장된 데이터를 JSON형태로 출력
	public ArrayList<BoardDTO> boardList(HttpServletRequest req){
		
		//파라미터 저장용 DTO객체
		ParameterDTO parameterDTO = new ParameterDTO();
		//검색필드 저장
		parameterDTO.setSearchField(req.getParameter("searchField"));
		//검색어 저장
		ArrayList<String> searchLists = null;
		
		if(req.getParameter("searchTxt") != null) {
			/*
			검색어가 2개 이상인 경우 스페이스로 구분하여 전송하면 split을 통해
			문자열을 분리한 후 List에 하나씩 추가한다.
			 */
			searchLists = new ArrayList<String>();
			String[] sTxtArray = req.getParameter("searchTxt").split(" ");
			for(String str : sTxtArray) {
				searchLists.add(str);
			}
		}
		
		
		parameterDTO.setSearchTxt(searchLists);
		System.out.println("검색어 : " + searchLists);
		
		//게시물 갯수 카운트
		int totalRecordCount = sqlSession.getMapper(IBoardDAO.class).getTotalCount(parameterDTO);
		
		//한페이지에 출력할 게시물의 갯수
		int pageSize = 10;
		//int blockPage = 2; // 페이지 번호를 출력하지 않으므로 여기서는 필요없음
		
		//페이지수 계산
		int totalPage = (int)Math.ceil((double)totalRecordCount/pageSize);
		//현재 페이지번호 가져오기
		int nowPage = (req.getParameter("nowPage")==null || req.getParameter("nowPage").equals("")) 
				? 1 : Integer.parseInt(req.getParameter("nowPage"));
		
		//게시물의 구간 계산
		int start = (nowPage-1) * pageSize +1 ;
		int end = nowPage * pageSize;
		
		//DTO에 저장
		parameterDTO.setStart(start);
		parameterDTO.setEnd(end);
		
		//현재 페이지에 해당하는 레코드 추출
		ArrayList<BoardDTO> lists = sqlSession.getMapper(IBoardDAO.class).listPage(parameterDTO);
		
		//내용에 대해 줄바꿈 처리
		for(BoardDTO dto : lists) {
			String temp = dto.getContent().replace("\r\n", "<br/>");
			dto.setContent(temp);
		}
		
		//List객체를 반환한다. 따라서 JSON배열 형태로 출력된다.
		return lists;
	}
	
	/*
	게시물 상세보기는 게시물 하나를 출력하므로 Map컬렉션을 통해
	JSON객체 형태로 출력한다.
	 */
	@RequestMapping("/restapi/boardView.do")
	@ResponseBody
	public Map<String, String> modify(ParameterDTO parameterDTO){
		ArrayList<BoardDTO> record = sqlSession.getMapper(IBoardDAO.class).view(parameterDTO);
		
		Map<String, String> map = new HashMap<String, String>();
		for(BoardDTO dto : record) {
			map.put("num", dto.getNum());
			map.put("title", dto.getTitle());
			map.put("content", dto.getContent());
			map.put("id", dto.getId());
			map.put("postdate", dto.getPostdate().toString());
			map.put("visitcount", dto.getVisitcount());
		}
		
		return map;
	}
}
