package com.taotao.cloud.payment.application.service;

import com.taotao.boot.ddd.model.application.service.CommandService;

public interface BussinessService extends CommandService {
    public boolean saveOrder() ;
}
