package com.bonita.vacation_request;

import java.util.HashMap;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.platform.LoginException;
import org.bonitasoft.engine.platform.LogoutException;
import org.bonitasoft.engine.session.Session;
import org.bonitasoft.engine.util.APITypeManager;

public class Authentication {

    public void connectToBonitaRuntime(APIClient apiClient, String username) {
        HashMap<String, String> settings = new HashMap<String, String>();
        settings.put("server.url", "http://localhost:12731");
        settings.put("application.name", "bonita");

        settings.put("basicAuthentication.active", "true");
        settings.put("basicAuthentication.username", "http-api");
        settings.put("basicAuthentication.password", "h11p-@p1");

        APITypeManager.setAPITypeAndParams((ApiAccessType.HTTP), settings);

        try {
            apiClient.login(username, "bpm");
            System.out.println("Login ok");
        } catch (LoginException e) {
            throw new RuntimeException("connectToBonitaRuntime error", e);
        }
    }

    public void logout(APIClient apiClient) throws LogoutException {
        apiClient.logout();
    }

    public Session getSession(APIClient apiClient) {
        Session session = apiClient.getSession();
        return session;
    }
}
