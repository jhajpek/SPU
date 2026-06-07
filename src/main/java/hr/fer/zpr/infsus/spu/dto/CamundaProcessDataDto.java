package hr.fer.zpr.infsus.spu.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CamundaProcessDataDto {

    private String eventName;
    private Long dvoranaId;
    private LocalDateTime eventDate;
    private String dvoranaNaziv;
}