/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages.accounts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.SessionManager;
import org.banque.client.pages.WebPage;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.ClientDTO;

/**
 *
 * @author bjurkovski
 */
public class AddRemoveAccountsPage extends WebPage {
    private VerticalPanel vPanel;
    private Label pageTitle;
    private Label addTitle;
    private Label removeTitle;
    private Grid addAccountsGrid;
    private Grid removeAccountsGrid;
    private TextBox balanceBox;
    private ListBox accountsList;
    private ListBox clientsList;
    private CheckBox emailWhenNegative;
    private Button addAccountButton;
    private Button removeAccountButton;
    
    private AsyncCallback<Void> updateAccountsCallback;
    
    private void updateClientsList() {
        final AsyncCallback<List<ClientDTO>> fillClientsList = new AsyncCallback<List<ClientDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<ClientDTO> result) {
                clientsList.clear();
                for(ClientDTO c : result) {
                    clientsList.addItem(c.getName() + " " + c.getLastName() 
                            + " (ID  " + String.valueOf(c.getId()) + ")",
                            String.valueOf(c.getId()));
                }
            }
        };
        
        getService().findAllClients(fillClientsList);
    }
    
    private void updateAccountsList() {
        AsyncCallback<List<AccountDTO>> updateAccountsListCallback = new AsyncCallback<List<AccountDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<AccountDTO> result) {
                accountsList.clear();
                for(AccountDTO a : result) {
                    ClientDTO c = a.getOwner();
                    String cName = c.getName() + " " + c.getLastName();
                    accountsList.addItem(String.valueOf(a.getId()) + " (" + cName + ")",
                                        String.valueOf(a.getId()));
                }
            }
        };
        
        getService().findAllAccounts(updateAccountsListCallback);
    }
    
    public static BanqueServiceAsync getService() {
        return GWT.create(BanqueService.class);
    }
    
    public AddRemoveAccountsPage() {
        setupPage();
    }
    
    public void setupPage() {
        vPanel = new VerticalPanel();
        pageTitle = new Label("Manage Accounts");
        addTitle = new Label("Add Account");
        removeTitle = new Label("Remove Account");
        balanceBox = new TextBox();
        accountsList = new ListBox();
        clientsList = new ListBox();
        accountsList.setVisibleItemCount(15);
        emailWhenNegative = new CheckBox("send warn on negative balance");
        addAccountButton = new Button("Add Account");
        removeAccountButton = new Button("Remove Account");
        
        pageTitle.addStyleName("title");
        addTitle.addStyleName("title");
        removeTitle.addStyleName("title");
        
        updateAccountsCallback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Void result) {
                updateAccountsList();
            }
        };
        
        addAccountButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                try {
                    int selectedIdx = clientsList.getSelectedIndex();
                    Long clientId = Long.valueOf(clientsList.getValue(selectedIdx));
                    getService().createAccount(clientId, updateAccountsCallback);
                } catch(IndexOutOfBoundsException e) {
                }
            }
        });
        
        removeAccountButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int selectedIdx = accountsList.getSelectedIndex();
                Long accountId = Long.valueOf(accountsList.getValue(selectedIdx));
                getService().removeAccount(accountId, updateAccountsCallback);
            }
        });
        
        vPanel.add(pageTitle);
        final HorizontalPanel hPanel = new HorizontalPanel();
        
        addAccountsGrid = new Grid(5,2);
        addAccountsGrid.setWidget(0, 0, addTitle);
        addAccountsGrid.setWidget(1, 0, new Label("Client: "));
        addAccountsGrid.setWidget(1, 1, clientsList);
        addAccountsGrid.setWidget(2, 0, new Label("Initial Balance: "));
        addAccountsGrid.setWidget(2, 1, balanceBox);
        addAccountsGrid.setWidget(3, 0, emailWhenNegative);
        addAccountsGrid.setWidget(4, 0, addAccountButton);
        
        removeAccountsGrid = new Grid(3,2);
        removeAccountsGrid.setWidget(0, 0, removeTitle);
        removeAccountsGrid.setWidget(1, 0, new Label("Account: "));
        removeAccountsGrid.setWidget(1, 1, accountsList);
        removeAccountsGrid.setWidget(2, 0, removeAccountButton);
        
        hPanel.add(addAccountsGrid);
        hPanel.add(removeAccountsGrid);
        vPanel.add(hPanel);
    }

    @Override
    public Widget getWidget() {
        updateClientsList();
        updateAccountsList();
        return vPanel;
    }
    
}
