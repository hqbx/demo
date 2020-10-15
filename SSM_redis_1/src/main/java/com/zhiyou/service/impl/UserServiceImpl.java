package com.zhiyou.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhiyou.dao.UserDao;
import com.zhiyou.model.User;
import com.zhiyou.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserDao dao;
	public void add(User user) {
		dao.add(user);
		System.out.println("service÷¥––add");
	}
	
	public void update(User user) {
		dao.update(user);
		System.out.println("service÷¥––update");
	}
	//@Transactional
	public void delete(int id) {
		dao.delete(id);
		
	}
	
	public List<User> select() {
		return dao.select();
	}

	

	public User selectById(int id) {
		
		return dao.selectById(id);
	}

}
