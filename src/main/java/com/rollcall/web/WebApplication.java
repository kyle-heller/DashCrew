package com.rollcall.web;

import com.rollcall.web.controller.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
public class WebApplication {

	@Autowired
	AuthController authController;
	@Autowired
	CommentController commentController;

	@Autowired
	EventController eventController;

	@Autowired
	GameController gameController;
	@Autowired
	GeoController GeoController;
	@Autowired
	GroupController groupController;
	@Autowired
	HomeController homeController;
	@Autowired
	ProfileController profileController;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

}
