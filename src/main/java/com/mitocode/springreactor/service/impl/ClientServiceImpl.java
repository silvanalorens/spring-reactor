package com.mitocode.springreactor.service.impl;

import com.mitocode.springreactor.model.Client;
import com.mitocode.springreactor.repo.IClientRepo;
import com.mitocode.springreactor.repo.IGenericRepo;
import com.mitocode.springreactor.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //if not use it, best implement constructor inject
public class ClientServiceImpl extends CRUDImpl<Client,String> implements IClientService {
    private final IClientRepo repo;

    @Override
    protected IGenericRepo<Client, String> getRepo() {
        return repo;
    }
}
