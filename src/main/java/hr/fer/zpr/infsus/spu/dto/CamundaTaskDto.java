package hr.fer.zpr.infsus.spu.dto;

import lombok.Data;

@Data
public class CamundaTaskDto {

	private String id;
	private String name;
	private String assignee;
	private String processInstanceId;
	private CamundaProcessDataDto processData;
}