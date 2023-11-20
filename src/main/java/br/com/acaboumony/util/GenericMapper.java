package br.com.acaboumony.util;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class GenericMapper<S, T> {
    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }

    public T mapDtoToModel(S dto, Class<T> targetClass) {
        return this.modelMapper().map(dto, targetClass);
    }

    public T mapModelToDto(S model, Class<T> targetClass) {
        return this.modelMapper().map(model, targetClass);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> this.modelMapper().map(element, targetClass))
                .collect(Collectors.toList());
    }

    public <S, T> Page<S> listToList(Page<T> source, Class<S> targetClass) {
        return source
                .map(element -> this.modelMapper().map(element, targetClass));
    }
}
