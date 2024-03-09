package com.rollcall.web.services;

import java.net.MalformedURLException;

public interface BggApiService {

    Long parseBggLink(String bggLink) throws MalformedURLException;

}
