/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.SessionManager;
import org.banque.client.pages.clients.AddRemoveClientsPage;
import org.banque.client.pages.clients.SearchClientsPage;

/**
 *
 * @author bjurkovski
 */
public class ManageClientsPage extends WebPage {
    private MenuBar menu = new MenuBar();
    private DockPanel panel = new DockPanel();
    private AddRemoveClientsPage addRemovePage = new AddRemoveClientsPage();
    private SearchClientsPage searchPage = new SearchClientsPage();
    private Widget currentPage = null;
    
    public class MenuAction implements Command {
        private String option = null;
        
        public MenuAction(String option) {
            this.option = option;
        }
        
        @Override
        public void execute() {
            if(currentPage != null) {
                panel.remove(currentPage);
            }
            
            if(option == "AddRemove Clients") {
                currentPage = addRemovePage.getWidget();
            }
            else if(option == "Search Clients") {
                currentPage = searchPage.getWidget();
            }
            
           panel.add(currentPage, DockPanel.CENTER);
        }
    }
    
    public ManageClientsPage() {
        setupPage();
    }
    
    public void setupPage() {
        menu = new MenuBar();
        panel = new DockPanel();
        addRemovePage = new AddRemoveClientsPage();
        searchPage = new SearchClientsPage();
        currentPage = null;
    
        menu.addItem(new MenuItem("Add Client", new MenuAction("AddRemove Clients")));
        menu.addItem(new MenuItem("Search Client", new MenuAction("Search Clients")));
        menu.setWidth("960px");
        
        panel.add(menu, DockPanel.NORTH);
    }
    
    public Widget getWidget() {
        // Refresh the page
        if(currentPage != null)
            currentPage = currentPage.asWidget();
        return panel;
    }
}
