package com.bonita.vacation_request;

import java.io.Serializable;
import java.util.HashMap;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessExecutionException;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.platform.LogoutException;

public class CancelVacationRequest {
    public static void main(String[] args) throws LogoutException, ProcessDefinitionNotFoundException {
        Authentication auth = new Authentication();
        APIClient apiClient = new APIClient();
        auth.connectToBonitaRuntime(apiClient, "walter.bates");
        ProcessAPI processApi = apiClient.getProcessAPI();
        long processDefinitionId = processApi.getProcessDefinitionId("Cancel Vacation Request", "1.0");

        String vacationRequestPersistenceId = "25";

        HashMap<String, Serializable> contract = new HashMap<>();
        contract.put("vacationRequestIdContract", vacationRequestPersistenceId);
        try {
            ProcessInstance processInstance = processApi.startProcessWithInputs(processDefinitionId, contract);
            long caseId = processInstance.getId();

            System.out.println("processInstanceId: " + caseId
                    + "\n processInstance " + processInstance.toString());

        } catch (ProcessDefinitionNotFoundException
                | ProcessActivationException
                | ProcessExecutionException
                | ContractViolationException e) {
            System.out.println(e);
        }

        auth.logout(apiClient);
    }
}
