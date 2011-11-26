/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
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
    
    protected Widget currentPage = null;
    protected ManageClientsPage manageClientsPage = new ManageClientsPage();
    protected ManageAccountsPage manageAccountsPage = new ManageAccountsPage();
    
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
        
        menuPanel.add(new Label("Societé Enecarré"));
        menuPanel.add(menuBar);

        mainPanel.add(menuPanel, DockPanel.NORTH);
    }
    
    public Widget getWidget() {
        return mainPanel;
    }
}