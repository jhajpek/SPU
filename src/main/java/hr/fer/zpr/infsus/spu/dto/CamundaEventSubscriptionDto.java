package hr.fer.zpr.infsus.spu.dto;

import lombok.Data;

@Data
public class CamundaEventSubscriptionDto {

	private String id;

	private String eventType;

	private String eventName;

	private String executionId;

	private String processInstanceId;

	private String activityId;
}