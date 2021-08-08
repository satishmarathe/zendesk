package com.zensearch.repo;

import java.util.List;

import com.zensearch.pojo.User;

public interface UserRepoIfc {
	
	String userFileName = "users.json";
	String encoding = "UTF-8";
	
	
	
	List<User> getAllUsers();
}
