package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.spring.service.InterBoardService;

@Controller
public class BoardController {

	@Autowired
	private  InterBoardService service;
	
	
}
