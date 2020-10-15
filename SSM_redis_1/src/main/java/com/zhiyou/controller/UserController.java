package com.zhiyou.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhiyou.model.User;
import com.zhiyou.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService service;
	
	
	@RequestMapping("show")
	public String show(HttpServletRequest req, HttpServletResponse resp){
		req.setAttribute("list", service.select());
		return "show";
	}
	
	@RequestMapping("add")
	public String add(User user, HttpServletRequest req, HttpServletResponse resp){
		service.add(user);
		return "forward:show";
	}
	
	@RequestMapping("delete")
	public String delete(int id, HttpServletRequest req, HttpServletResponse resp){
		service.delete(id);
		return "forward:show";
	}
	
	
	@RequestMapping("selectById")
	public String selectByID(int id, HttpServletRequest req, HttpServletResponse resp){
		req.setAttribute("user", service.selectById(id));
		return "update";
	}
	
	@RequestMapping("update")
	public String update(User user, HttpServletRequest req, HttpServletResponse resp){
		service.update(user);
		return "forward:show";
	}
	
}
