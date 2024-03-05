package academy.devdojo.mapper;


import academy.devdojo.domain.Producer;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProduceGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import academy.devdojo.response.ProducerPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProducerMapper {


    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
    Producer toProducer(ProducerPostRequest request);

    @Mapping(source ="createdAt", target = "createdAt")
    Producer toProducer(ProducerPutRequest request, LocalDateTime createdAt);

    ProducerPostResponse toProducerPostResponse(Producer producer);

    List<ProduceGetResponse> toProducerGetResponse(List<Producer> producers);
}
