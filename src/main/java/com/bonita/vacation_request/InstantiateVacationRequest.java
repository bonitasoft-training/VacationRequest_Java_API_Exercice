package com.bonita.vacation_request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessExecutionException;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.platform.LogoutException;

// mvn spring-boot:run -Dstart-class=com.bonita.vacation_request.InstantiateVacationRequest
public class InstantiateVacationRequest {
    public static void main(String[] args) throws LogoutException, ProcessDefinitionNotFoundException {
        Authentication auth = new Authentication();
        APIClient apiClient = new APIClient();
        auth.connectToBonitaRuntime(apiClient, "walter.bates");

        ProcessAPI processApi = apiClient.getProcessAPI();

        long processDefinitionId = processApi.getProcessDefinitionId("New Vacation Request", "1.1");
        System.out.println("processDefinitionId " + processDefinitionId);

        Map<String, Serializable> contractInputs = new HashMap<>();
        LocalDate startDate = LocalDate.of(2025, 03, 10);
        LocalDate returnDate = LocalDate.of(2025, 03, 13);
        contractInputs.put("startDateContract", startDate);
        contractInputs.put("returnDateContract", returnDate);
        contractInputs.put("numberOfDaysContract", 3);

        System.out.println("contractInputs: " + contractInputs);

        try {
            ProcessInstance processInstance = processApi.startProcessWithInputs(processDefinitionId,
                    contractInputs);
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