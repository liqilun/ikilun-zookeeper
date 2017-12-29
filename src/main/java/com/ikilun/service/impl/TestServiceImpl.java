package com.ikilun.service.impl;

import org.springframework.stereotype.Service;

import com.ikilun.service.TestService;

@Service
public class TestServiceImpl implements TestService{

	@Override
	public void test() {
		System.out.println("xxxxxxx");
	}

}
