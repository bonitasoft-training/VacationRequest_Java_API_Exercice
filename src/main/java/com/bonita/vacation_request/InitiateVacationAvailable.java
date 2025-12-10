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

public class InitiateVacationAvailable {
    public static void main(String[] args) throws LogoutException, ProcessDefinitionNotFoundException {
        Authentication auth = new Authentication();
        APIClient apiClient = new APIClient();
        auth.connectToBonitaRuntime(apiClient, "walter.bates");
        ProcessAPI processApi = apiClient.getProcessAPI();
        long processDefinitionId = processApi.getProcessDefinitionId("Initiate Vacation Available", "1.0");

        String toCancelCaseId = "1008";

        HashMap<String, Serializable> contract = new HashMap<>();
        contract.put("vacationRequestIdContract", toCancelCaseId);
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
