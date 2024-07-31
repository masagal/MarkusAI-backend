package tech.masagal.markusai.chat.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.masagal.markusai.request.RequestListDto;

public class ChoicesMessageWithPojoDto extends ChoicesMessage {
    @JsonProperty("content.requestList")
    RequestListDto requestListDto;
    @JsonProperty("content.messageToUser")
    String messageToUser;

    public ChoicesMessageWithPojoDto(RequestListDto requestListDto, String messageToUser) {
        this.messageToUser = messageToUser;
        this.requestListDto = requestListDto;
    }

    public String content() {
        return messageToUser;
    }
}
