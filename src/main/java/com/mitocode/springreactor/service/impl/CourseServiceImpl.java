package com.mitocode.springreactor.service.impl;

import com.mitocode.springreactor.model.Course;
import com.mitocode.springreactor.repo.ICursoRepo;
import com.mitocode.springreactor.repo.IGenericRepo;
import com.mitocode.springreactor.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends CRUDImpl<Course,String> implements ICourseService {
    private final ICursoRepo repo;

    @Override
    protected IGenericRepo<Course, String> getRepo() {
        return repo;
    }


}
