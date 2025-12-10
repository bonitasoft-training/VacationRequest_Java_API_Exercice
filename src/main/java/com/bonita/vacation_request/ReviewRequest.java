package com.bonita.vacation_request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.platform.LogoutException;
import org.bonitasoft.engine.session.Session;

// import org.springframework.boot.SpringApplication;

public class ReviewRequest {
    public static void main(String[] args) throws LogoutException {
        Authentication auth = new Authentication();
        APIClient apiClient = new APIClient();
        auth.connectToBonitaRuntime(apiClient, "helen.kelly");

        Session session = auth.getSession(apiClient);
        long connectedUserId = session.getUserId();
        ProcessAPI processApi = apiClient.getProcessAPI();

        try {

            List<HumanTaskInstance> taskList = processApi.getAssignedHumanTaskInstances(connectedUserId, 0, 20,
                    ActivityInstanceCriterion.LAST_UPDATE_ASC);

            System.out.println("taskList: " + taskList.size());
            if (taskList.size() == 0) {
                System.out.println("No task available for Helen Kelly");
                return;
            }
            long oldestCaseId = 0;
            oldestCaseId = taskList.get(0).getId();
            for (HumanTaskInstance task : taskList) {
                System.out.println("ReachStateDate: " + task.getReachedStateDate() + " taskId: " + task.getId()
                        + " caseId: " + task.getParentProcessInstanceId());
            }

            HashMap<String, Serializable> contract = new HashMap<>();
            contract.put("statusContract", "approved");
            contract.put("commentsContract", "Everybody needs days off");

            processApi.assignAndExecuteUserTask(connectedUserId, oldestCaseId, contract);
            System.out.println("The oldest user task with Id: " + oldestCaseId + " was executed successfully");
        } catch (UserTaskNotFoundException
                | FlowNodeExecutionException
                | ContractViolationException
                | UpdateException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        auth.logout(apiClient);
    }
}
