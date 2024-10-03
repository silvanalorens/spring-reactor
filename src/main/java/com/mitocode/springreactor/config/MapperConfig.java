package com.mitocode.springreactor.config;

import com.mitocode.springreactor.dto.TuitionDTO;
import com.mitocode.springreactor.model.Tuition;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean(name = "tuitionMapper")
    public ModelMapper tuitionMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //Escritura
        mapper.createTypeMap(TuitionDTO.class, Tuition.class)
                .addMapping(e -> e.getStudent().getFirsName(), (dest, v) -> dest.getStudent().setFirsName((String) v))
                .addMapping(e -> e.getStudent().getLastName(), (dest, v) -> dest.getStudent().setLastName((String) v))
                .addMapping(e -> e.getStudent().getDni(), (dest, v) -> dest.getStudent().setDni((String) v))
                .addMapping(e -> e.getStudent().getAge(), (dest, v) -> dest.getStudent().setAge((Integer) v));

        //Lectura
        mapper.createTypeMap(Tuition.class, TuitionDTO.class)
                .addMapping(e -> e.getStudent().getFirsName(), (dest, v) -> dest.getStudent().setFirsName((String) v))
                .addMapping(e -> e.getStudent().getLastName(), (dest, v) -> dest.getStudent().setLastName((String) v))
                .addMapping(e -> e.getStudent().getDni(), (dest, v) -> dest.getStudent().setDni((String) v))
                .addMapping(e -> e.getStudent().getAge(), (dest, v) -> dest.getStudent().setAge((Integer) v));

        return mapper;
    }
}
