package com.rollcall.web.services;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

public interface BggApiService {

    Long parseBggLink(String bggLink) throws MalformedURLException;

}
