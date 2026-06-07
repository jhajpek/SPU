package hr.fer.zpr.infsus.spu.service;

import java.util.List;
import java.util.Map;

import hr.fer.zpr.infsus.spu.dto.CamundaProcessDataDto;
import hr.fer.zpr.infsus.spu.dto.CamundaTaskDto;
import hr.fer.zpr.infsus.spu.dto.StartProcessCamundaDto;
import hr.fer.zpr.infsus.spu.dto.WaitingApprovalDto;

public interface CamundaService {

	void startProcess(StartProcessCamundaDto dto);

	List<CamundaTaskDto> getTasks();

	void completeTask(String taskId, Map<String, Object> variables);

	public void approve(boolean approved, String processInstanceId);

	List<CamundaTaskDto> getTasks(String assignee);

	CamundaProcessDataDto getVariables(String processInstanceId);

	List<WaitingApprovalDto> getWaitingApprovals();

	long countWaitingAdminApprovals();

	public long countTasks(String assignee);

	void createEventFromProcess(String processInstanceId);

	List<CamundaTaskDto> getTasksForUser(String username);

	public String getUserGroup(String username);

}