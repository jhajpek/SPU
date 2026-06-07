package hr.fer.zpr.infsus.spu.dto;

import lombok.Data;

@Data
public class WaitingApprovalDto {

	private String processInstanceId;

	private CamundaProcessDataDto processData;
}
