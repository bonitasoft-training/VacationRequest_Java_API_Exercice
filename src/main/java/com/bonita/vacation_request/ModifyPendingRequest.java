package com.bonita.vacation_request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
// import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessExecutionException;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
// import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.platform.LogoutException;
import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
// import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.Session;

public class ModifyPendingRequest {
    public static void main(String[] args)
            throws LogoutException, ProcessDefinitionNotFoundException, DataNotFoundException {
        Authentication auth = new Authentication();
        APIClient apiClient = new APIClient();
        auth.connectToBonitaRuntime(apiClient, "walter.bates");
        Session session = apiClient.getSession();
        long connectedUserId = session.getUserId();
        ProcessAPI processApi = apiClient.getProcessAPI();
        SearchOptionsBuilder searchOptionBuilder = new SearchOptionsBuilder(0, 10);
        searchOptionBuilder.filter(ProcessInstanceSearchDescriptor.NAME, "New Vacation Request");
        searchOptionBuilder.filter(ProcessInstanceSearchDescriptor.STARTED_BY, connectedUserId);
        searchOptionBuilder.sort(ProcessInstanceSearchDescriptor.START_DATE, Order.ASC);

        try {
            // SearchResult<ProcessInstance> processInstanceResults = processApi
            //         .searchOpenProcessInstances(searchOptionBuilder.done());
            // List<ProcessInstance> processInstanceList = processInstanceResults.getResult();
            // if (processInstanceList.size() == 0) {
            //     System.out.println("No vacation request available");
            //     return;
            // }
            // Connect to h2 console and get id of the persistenceId of the request to change
            String vacationRequestPersistenceId = String.valueOf(24);

            // for (ProcessInstance processInstance : processInstanceList) {
            //     System.out.println("processName: " + processInstance.getName() + " CaseId: " + processInstance.getId()
            //             + " StartedBy: " + processInstance.getStartedBy());
            // }

            long processDefinitionId = processApi.getProcessDefinitionId("Modify Pending Vacation Request", "1.0");
            System.out.println("processDefinitionId " + processDefinitionId);

            Map<String, Serializable> contractInputs = new HashMap<>();
            LocalDate startDate = LocalDate.of(2025, 04, 10);
            LocalDate returnDate = LocalDate.of(2025, 04, 14);

            contractInputs.put("vacationRequestIdContract", vacationRequestPersistenceId);
            contractInputs.put("startDateContract", startDate);
            contractInputs.put("returnDateContract", returnDate);
            contractInputs.put("numberOfDaysContract", 4);

            System.out.println("contractInputs: " + contractInputs);
            ProcessInstance processInstance = processApi.startProcessWithInputs(processDefinitionId,
                    contractInputs);
            long caseId = processInstance.getId();

            System.out.println("processInstanceId: " + caseId
                    + "\n processInstance " + processInstance.toString());

        } catch (ProcessDefinitionNotFoundException
                | ProcessActivationException
                | ProcessExecutionException
                | ContractViolationException e) {
            System.out.println("Errro: " + e);
        }

        auth.logout(apiClient);

    }
}
