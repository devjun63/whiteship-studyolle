package com.studyolle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SpringBootApplication
public class APP {

	public static void main(String[] args) {
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(1,2);
		SpringApplication.run(APP.class, args);
	}

}
