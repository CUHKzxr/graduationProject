package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface ShowDataService {
    public String updateDataLocal(String requestData);

    public void test();

    public String searchData(String requestData);

}
