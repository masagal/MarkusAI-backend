package org.example.groupbackend.request;


import java.util.List;

public record RequestListDto(List<RequestProduct> requests, Long userId) {

}
