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
import mybatis.IAndroidDAO;
import mybatis.IBoardDAO;
import mybatis.MemberVO;
import mybatis.ParameterDTO;

@Controller 
public class AndroidController {

	
	//회원리스트 배열형태로 가져오기
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping("/android/memberObject.do")
	@ResponseBody
	public Map<String, Object> memberObject(){
	
		Map<String, Object> map = new HashMap<String, Object>();
		
		ArrayList<MemberVO> list = sqlSession.getMapper(IAndroidDAO.class).memberList();
		
		map.put("memberList", list);
		return map;
	}
	
	
	@RequestMapping("/android/memberList.do")
	@ResponseBody
	public ArrayList<MemberVO> memberList(HttpServletRequest req){
		
		ArrayList<MemberVO> lists = sqlSession.getMapper(IAndroidDAO.class).memberList();
		
		return lists;
	}
	
	
	@RequestMapping("/android/memberLogin.do")
	@ResponseBody
	public Map<String, Object> memberLogin(MemberVO memberVO){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		MemberVO record = sqlSession.getMapper(IAndroidDAO.class).memberLogin(memberVO);
		System.out.println(record);
		
		if(record == null) {
			map.put("isLogin", 0);
		}
		else {
			map.put("isLogin", 1);
			map.put("memberInfo", record );
		}
		
		return map;
	}
	
} 
