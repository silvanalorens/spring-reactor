package com.mitocode.springreactor.service.impl;

import com.mitocode.springreactor.model.Tuition;
import com.mitocode.springreactor.repo.IGenericRepo;
import com.mitocode.springreactor.repo.ITuitionRepo;
import com.mitocode.springreactor.service.ITuitionService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor //if not use it, best implement constructor inject
public class TuitionServiceImpl extends CRUDImpl<Tuition,String> implements ITuitionService {
    private final ITuitionRepo repo;

    @Override
    protected IGenericRepo<Tuition, String> getRepo() {
        return repo;
    }

    @Override
    public Mono<byte[]> generateReport(String idTuition) {
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
    }
}
