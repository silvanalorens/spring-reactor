package com.mitocode.springreactor.service.impl;

import com.mitocode.springreactor.model.Course;
import com.mitocode.springreactor.model.Tuition;
import com.mitocode.springreactor.repo.ICourseRepo;
import com.mitocode.springreactor.repo.IGenericRepo;
import com.mitocode.springreactor.repo.IStudentRepo;
import com.mitocode.springreactor.repo.ITuitionRepo;
import com.mitocode.springreactor.service.ITuitionService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor //if not use it, best implement constructor inject
public class TuitionServiceImpl extends CRUDImpl<Tuition,String> implements ITuitionService {
    private final ITuitionRepo repo;
    private final IStudentRepo studentRepo;
    private final ICourseRepo courseRepo;
    @Override
    protected IGenericRepo<Tuition, String> getRepo() {
        return repo;
    }

    private Mono<Tuition> populateStudent(Tuition tuition) {
        return studentRepo.findById(tuition.getStudent().getId())
                .map(student -> {
                    tuition.setStudent(student);
                    return tuition;
                });
    }

    private Mono<Tuition> populateCourses(Tuition tuition) {
        List<Mono<Course>> list = tuition.getCourses().stream() //Stream<Course>
                .map(item -> courseRepo.findById(item.getId()) //Mono<Course>
                ).toList();

        return Mono.when(list).then(Mono.just(tuition));
    }

    private byte[] generatePDF(Tuition tuition) {
        try (InputStream stream = getClass().getResourceAsStream("/lista.jrxml")) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("txt_course", tuition.getRegisterDate());

            JasperReport report = JasperCompileManager.compileReport(stream);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(tuition.getCourses()));
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    @Override
    public Mono<byte[]> generateReport(String idTuition) {
        long startTime = System.currentTimeMillis();
        return repo.findById(idTuition)
                .subscribeOn(Schedulers.single())
                .publishOn(Schedulers.newSingle("th-data"))
                .flatMap(tuition ->
                        //zip permite avanzar cuando ambos hayan terminado
                        Mono.zip(
                                populateStudent(tuition),
                                populateCourses(tuition),
                                (populatedStudent, populatedCourses) -> populatedCourses // Retorna la factura con todo agregado
                        )
                )
                .publishOn(Schedulers.boundedElastic())
                .map(this::generatePDF)
                .onErrorResume(e -> Mono.empty())
                .doOnSuccess(inv -> {
                    long endTime = System.currentTimeMillis();
                    System.out.println("Total Time: " + (endTime - startTime) + " ms");
                });
    }
    /*public Mono<byte[]> generateReportv1(String idTuition) {
        //v1
        return repo.findById(idTuition)
                .map(tuitions -> {
                    try {
                        Map<String, Object> params = new HashMap<>();
                        params.put("txt_course", tuitions.getRegisterDate() + " "+ tuitions.getStatus());

                        InputStream stream = getClass().getResourceAsStream("/lista.jrxml");
                        JasperReport report1 = JasperCompileManager.compileReport(stream);
                        JasperPrint print = JasperFillManager.fillReport(report1, params, new JRBeanCollectionDataSource(tuitions.getCourses()));
                        return JasperExportManager.exportReportToPdf(print);

                    }
                    catch (Exception e){
                        return new byte[0];
                    }
                });

        //
    }*/
}
