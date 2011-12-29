/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Date;
import java.util.List;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.SessionManager;
import org.banque.dtos.ClientDTO;
import org.banque.dtos.PersonDTO.Gender;
import org.banque.entities.Person;

/**
 *
 * @author bjurkovski
 */
public class MainPage extends WebPage {
    protected DockPanel mainPanel = new DockPanel();
    protected MenuBar menuBar = new MenuBar();
    protected VerticalPanel menuPanel = new VerticalPanel();
    protected TextBox loginBox = new TextBox();
    protected PasswordTextBox passwBox = new PasswordTextBox();
    protected Anchor loginLink = new Anchor("Login");
    protected Anchor logoutLink = new Anchor("Logout");
    
    protected Widget currentPage = null;
    protected ManageClientsPage manageClientsPage = new ManageClientsPage();
    protected ManageAccountsPage manageAccountsPage = new ManageAccountsPage();
    protected TransactionsPage transactionsPage = new TransactionsPage();
    
    public class MenuAction implements Command {
        private String option = null;
        
        public MenuAction(String option) {
            this.option = option;
        }
        
        @Override
        public void execute() {
            if(currentPage != null) {
                mainPanel.remove(currentPage);
            }
            
            if(option == "Manage Clients") {
                currentPage = manageClientsPage.getWidget();
            }
            else if(option == "Manage Accounts") {
                currentPage = manageAccountsPage.getWidget();
            }
            else if(option == "Transactions") {
                currentPage = transactionsPage.getWidget();
            }
            
            mainPanel.add(currentPage, DockPanel.CENTER);
        }
    }
    
    public MainPage() {
        sessionManager.registerWebPage(this);
        setupPage();
    }
    
    public void setupPage() {
        manageClientsPage.setupPage();
        manageAccountsPage.setupPage();
        transactionsPage.setupPage();
        
        mainPanel.clear();
    
        /*
         * http://stackoverflow.com/questions/1061705/multiple-pages-tutorial-in-google-web-toolkit-gwt
         * http://code.google.com/webtoolkit/articles/mvp-architecture.html
         */
        menuBar.clearItems();
        if(sessionManager.isAdmin()) {
            menuBar.addItem(new MenuItem("Manage Clients", new MenuAction("Manage Clients")));
        }
        
        if(sessionManager.isLoggedIn()) {
            menuBar.addItem(new MenuItem("Manage Accounts", new MenuAction("Manage Accounts")));
            menuBar.addItem(new MenuItem("Transactions", new MenuAction("Transactions")));
        }
        menuBar.setWidth("960px");
        
        Label title = new Label("Banque Alpin");
        title.setStyleName("title");
        
        loginLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                try {
                    if(sessionManager.login(loginBox.getValue(), passwBox.getValue())) {
                        loginBox.setText("");
                        passwBox.setText("");
                    }
                } catch(Exception e) {
                    mainPanel.add(new Label(e.toString()), DockPanel.WEST);
                }
            }
        });
        
        logoutLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                sessionManager.logout();
            }
        });
        
        HorizontalPanel loginPanel = new HorizontalPanel();
        if(sessionManager.isLoggedIn()) {
            loginPanel.add(new Label("Logged in as " + sessionManager.getUsername() + " "));
            if(sessionManager.isAdmin()) {
                loginPanel.add(new Label("(Administrator) "));
            }
            loginPanel.add(logoutLink);
        }
        else {
            loginPanel.add(new Label("e-mail: "));
            loginPanel.add(loginBox);
            loginPanel.add(new Label("password: "));
            loginPanel.add(passwBox);
            loginPanel.add(loginLink);
            loginPanel.setCellVerticalAlignment(loginPanel.getWidget(0), HorizontalPanel.ALIGN_MIDDLE);
            loginPanel.setCellVerticalAlignment(loginPanel.getWidget(2), HorizontalPanel.ALIGN_MIDDLE);
            loginPanel.setCellVerticalAlignment(loginPanel.getWidget(4), HorizontalPanel.ALIGN_MIDDLE);
        }
        loginPanel.setSpacing(5);
        
        menuPanel.clear();
        menuPanel.add(loginPanel);
        menuPanel.add(title);
        //if(s == SessionManager.UserRole.ADMIN)
        //    menuPanel.add(new Label("eh admin"));
        menuPanel.add(menuBar);
        
        menuPanel.setCellHorizontalAlignment(menuPanel.getWidget(0), VerticalPanel.ALIGN_RIGHT);

        mainPanel.add(menuPanel, DockPanel.NORTH);
    }
    
    public BanqueServiceAsync getService() {
        return GWT.create(BanqueService.class);
    }
    
    public Widget getWidget() {
        return mainPanel;
    }
}