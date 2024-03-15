package com.rollcall.web.services;

import java.util.List;

public interface GeolocationService {

    List<Integer> findClosestZips(int zip);
}
