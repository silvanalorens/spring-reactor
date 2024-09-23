package com.mitocode.springreactor.service.impl;

import com.mitocode.springreactor.model.Student;
import com.mitocode.springreactor.repo.IEstudianteRepo;
import com.mitocode.springreactor.repo.IGenericRepo;
import com.mitocode.springreactor.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //if not use it, best implement constructor inject
public class StudentServiceImpl extends CRUDImpl<Student,String> implements IStudentService {
    private final IEstudianteRepo repo;

    @Override
    protected IGenericRepo<Student, String> getRepo() {
        return repo;
    }
}
