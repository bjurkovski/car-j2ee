/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages;

import com.google.gwt.user.client.Command;
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

/**
 *
 * @author bjurkovski
 */
public class MainPage implements WebPage {
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
        /*
         * http://stackoverflow.com/questions/1061705/multiple-pages-tutorial-in-google-web-toolkit-gwt
         * http://code.google.com/webtoolkit/articles/mvp-architecture.html
         */
        menuBar.addItem(new MenuItem("Manage Clients", new MenuAction("Manage Clients")));
        menuBar.addItem(new MenuItem("Manage Accounts", new MenuAction("Manage Accounts")));
        menuBar.addItem(new MenuItem("Transactions", new MenuAction("Transactions")));
        menuBar.setWidth("960px");
        
        Label title = new Label("Banque du Brésil");
        title.setStyleName("title");
        
        final HorizontalPanel loginPanel = new HorizontalPanel();
        loginPanel.add(loginBox);
        loginPanel.add(passwBox);
        loginPanel.add(loginLink);
        
        menuPanel.add(loginPanel);
        menuPanel.add(title);
        menuPanel.add(menuBar);
        
        menuPanel.setCellHorizontalAlignment(menuPanel.getWidget(0), VerticalPanel.ALIGN_RIGHT);

        mainPanel.add(menuPanel, DockPanel.NORTH);
    }
    
    public Widget getWidget() {
        return mainPanel;
    }
}