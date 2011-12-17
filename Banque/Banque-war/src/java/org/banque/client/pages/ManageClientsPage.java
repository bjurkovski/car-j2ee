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
import org.banque.client.pages.clients.AddRemoveClientsPage;

/**
 *
 * @author bjurkovski
 */
public class ManageClientsPage implements WebPage {
    private MenuBar menu = new MenuBar();
    private DockPanel panel = new DockPanel();
    private BanqueServiceAsync b = GWT.create(BanqueService.class);
    private AddRemoveClientsPage addRemovePage = new AddRemoveClientsPage();
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
                //currentPage = manageAccountsPage.getWidget();
            }
            
           panel.add(currentPage, DockPanel.CENTER);
        }
    }
    
    public ManageClientsPage() {
        menu.addItem(new MenuItem("Add Client", new MenuAction("AddRemove Clients")));
        menu.addItem(new MenuItem("Search Client", new MenuAction("Search Clients")));
        menu.setWidth("960px");
        
        panel.add(menu, DockPanel.NORTH);
        /*
        panel.add(new Label("Manage Clients"));
        
        b.myMethod("uma chamada de teste", new AsyncCallback<String>() {
            public void onSuccess(String result) {
                panel.add(new Label(result));
            }
            
            public void onFailure(Throwable caught) {
                panel.add(new Label("deu caca"));
            }
        });
         *
         */
    }
    
    public Widget getWidget() {
        return panel;
    }
}
