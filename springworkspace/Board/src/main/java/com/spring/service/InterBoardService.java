package com.spring.service;

import java.util.HashMap;
import java.util.List;

import com.spring.model.TestVO;

public interface InterBoardService {

	int test_insert();
	
	HashMap<String, List<TestVO>> test_select();

	int test_insert(HashMap<String, String> paraMap);
	
}


