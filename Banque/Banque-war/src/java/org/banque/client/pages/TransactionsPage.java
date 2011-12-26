/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.pages.accounts.SearchAccountsPage;
import org.banque.client.pages.transaction.MakeTransactionPage;
import org.banque.client.pages.transaction.TransactionsLogPage;

/**
 *
 * @author bjurkovski
 */
public class TransactionsPage implements WebPage {
    private MenuBar menu = new MenuBar();
    private DockPanel panel = new DockPanel();
    private BanqueServiceAsync b = GWT.create(BanqueService.class);
    private MakeTransactionPage makeTransactionPage = new MakeTransactionPage();
    private TransactionsLogPage logPage = new TransactionsLogPage();
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
            
            if(option == "Make Transaction") {
                currentPage = makeTransactionPage.getWidget();
            }
            else if(option == "Transaction Log") {
                currentPage = logPage.getWidget();
            }
            
           panel.add(currentPage, DockPanel.CENTER);
        }
    }
    
    public TransactionsPage() {
        menu.addItem(new MenuItem("Make Transaction", new MenuAction("Make Transaction")));
        menu.addItem(new MenuItem("View Log", new MenuAction("Transaction Log")));
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
