package org.example.groupbackend.request;


import java.util.List;

public record RequestListDto(List<RequestProductDto> requests, Long userId) {

}
