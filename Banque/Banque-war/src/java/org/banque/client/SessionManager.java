/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.banque.client.pages.WebPage;

/**
 *
 * @author bjurkovski
 */
public class SessionManager {
    public enum UserRole {
        ORDINARY_USER,
        REGISTERED_USER,
        ADMIN_USER
    };
    
    private static UserRole role = UserRole.ORDINARY_USER;
    private static List<WebPage> pages = new ArrayList<WebPage>();
    
    public SessionManager() {
    }
    
    public void registerWebPage(WebPage page) {
        boolean alreadyAdded = false;
        for(WebPage p : pages) {
            if(page == p) {
                alreadyAdded = true;
                break;
            }
        }
        
        if(!alreadyAdded)
            pages.add(page);
    }
    
    public boolean login(String email, String password) {
        getService().login(email, password, new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result[]) {
                String sessionId = result[0];
                if(sessionId == null) {
                    role = UserRole.ORDINARY_USER;
                }
                else {
                    String userEmail = result[1];
                    if(result[2].equals("admin")) {
                        role = UserRole.ADMIN_USER;
                    }
                    else {
                        role = UserRole.REGISTERED_USER;
                    }
                    
                    //duration remembering login. 2 weeks in this example.
                    final long DURATION = 1000 * 60 * 60 * 24 * 14;
                    Date expires = new Date(System.currentTimeMillis() + DURATION);
                    Cookies.setCookie("sid", sessionId, expires, null, "/", false);
                    Cookies.setCookie("uem", userEmail, expires, null, "/", false);
                }
                
                for(WebPage p : pages) {
                    p.setupPage();
                }
            }
        });
        
        return true;
    }
    
    public void logout() {
        getService().logout(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) { }
            @Override
            public void onSuccess(Void result) { }
        });
        
        role = UserRole.ORDINARY_USER;
        Cookies.removeCookie("sid");
        Cookies.removeCookie("uem");
        
        for(WebPage p : pages) {
            p.setupPage();
        }
    }
    
    private void refreshSession() {
        String sessionId = Cookies.getCookie("sid");
        //if ( sessionID != null ) checkWithServerIfSessionIdIsStillLegal();
        //else displayLoginBox();
        if(sessionId == null) {
            role = UserRole.ORDINARY_USER;
        }
    }

    public UserRole getUserRole() {
        refreshSession();
        return role;
    }
    
    public boolean isLoggedIn() {
        refreshSession();
        if(role.equals(UserRole.REGISTERED_USER) || role.equals(UserRole.ADMIN_USER))
            return true;
        else
            return false;
    }
    
    public boolean isAdmin() {
        refreshSession();
        if(role.equals(UserRole.ADMIN_USER))
            return true;
        else
            return false;
    }
    
    public String getUsername() {
        return Cookies.getCookie("uem");
    }
    
    private BanqueServiceAsync getService() {
        return GWT.create(BanqueService.class);
    }
}
