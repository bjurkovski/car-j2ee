/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages.transaction;

import org.banque.client.pages.accounts.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.SessionManager;
import org.banque.client.pages.WebPage;
import org.banque.client.ui.PopupWidget;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.ClientDTO;
import org.banque.dtos.TransactionDTO;
import org.banque.managers.interfaces.IAccountManagerLocal;

/**
 *
 * @author bjurkovski
 */
public class MakeTransactionPage extends WebPage {
    private static final int SRC = 0;
    private static final int DST = 1;
    
    private VerticalPanel vPanel;
    private Label pageTitle;
    private Label sourceTitle;
    private Label destinationTitle;
    private Grid sourceGrid;
    private Grid destinationGrid;
    private TextBox[] searchBox;
    private Button[] filterButton;
    private ListBox[] searchCriteriaList;
    private ListBox[] accountsList;
    private TextBox amountBox;
    private Button confirmTransactionButton;
    private PopupWidget popupWidget;
    
    private AsyncCallback<List<AccountDTO>>[] updateAccountsListCallback;
    
    private void filterAccounts(int srcOrDst) {
        if(srcOrDst==SRC || srcOrDst==DST) {
            int searchCriteriaIdx = searchCriteriaList[srcOrDst].getSelectedIndex();
            int searchCriteria = Integer.valueOf(searchCriteriaList[srcOrDst].getValue(searchCriteriaIdx));
            
            if(searchCriteria == IAccountManagerLocal.ALL)
                findAllAccounts(srcOrDst);
            else
                getService().findAccountsByCriteria(searchBox[srcOrDst].getText(), searchCriteria, updateAccountsListCallback[srcOrDst]);
        }
    }
    
    private void findAllAccounts(int srcOrDst) {
        if(srcOrDst==SRC || srcOrDst==DST) {
            getService().findAllAccounts(updateAccountsListCallback[srcOrDst]);
        }
    }
    
    private void updateListedAccounts() {
        ArrayList<Long>[] ids = new ArrayList[2];
        for(int i=0; i<2; i++) {
            ids[i] = new ArrayList<Long>();
            for(int j=0; j<accountsList[i].getItemCount(); j++) {
                Long accountId = Long.valueOf(accountsList[i].getValue(j));
                ids[i].add(accountId);
            }
            getService().findAccounts(ids[i], updateAccountsListCallback[i]);
        }
    }
    
    public static BanqueServiceAsync getService() {
        return GWT.create(BanqueService.class);
    }
    
    public MakeTransactionPage() {
        setupPage();
    }
    
    private void showConfirmBox(ClickHandler onConfirm) {
        popupWidget = new PopupWidget("Confirmation box", true);
        popupWidget.setContent(new Label("Are you sure?"));
        popupWidget.show();

        popupWidget.addOKHandler(onConfirm);

        popupWidget.addCancelHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popupWidget.hide();
            }
        });
    }
    
    public void setupPage() {
        vPanel = new VerticalPanel();
        pageTitle = new Label("Make Transaction");
        sourceTitle = new Label("Source");
        destinationTitle = new Label("Destination");
        
        searchBox = new TextBox[2];
        for(int i=0; i<2; i++)
            searchBox[i] = new TextBox();
        
        filterButton = new Button[2];
        for(int i=0; i<2; i++)
            filterButton[i] = new Button("Filter");
        
        filterButton[SRC].addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                filterAccounts(SRC);
            }
        });
        
        filterButton[DST].addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                filterAccounts(DST);
            }
        });
        
        searchCriteriaList = new ListBox[2];
        for(int i=0; i<2; i++) {
            searchCriteriaList[i] = new ListBox();
            searchCriteriaList[i].addItem("All", String.valueOf(IAccountManagerLocal.ALL));
            searchCriteriaList[i].addItem("ID", String.valueOf(IAccountManagerLocal.ID));
            searchCriteriaList[i].addItem("Negative Balance", String.valueOf(IAccountManagerLocal.BALANCE_NEGATIVE));
            searchCriteriaList[i].addItem("Positive Balance", String.valueOf(IAccountManagerLocal.BALANCE_POSITIVE));
            if(i==DST || sessionManager.isAdmin()) {
                searchCriteriaList[i].addItem("Client Name", String.valueOf(IAccountManagerLocal.PRENOM_CLIENT));
                searchCriteriaList[i].addItem("Client Last Name", String.valueOf(IAccountManagerLocal.NOM_CLIENT));
            }
            
        }
        
        accountsList = new ListBox[2];
        for(int i=0; i<2; i++) {
            accountsList[i] = new ListBox();
            accountsList[i].setVisibleItemCount(15);
        }
        
        amountBox = new TextBox();
        confirmTransactionButton = new Button("Confirm");
        
        pageTitle.addStyleName("title");
        sourceTitle.addStyleName("title");
        destinationTitle.addStyleName("title");
        
        updateAccountsListCallback = new AsyncCallback[2];
        for(int i=0; i<2; i++) {
            final ListBox currentList;
            currentList = accountsList[i];
            updateAccountsListCallback[i] = new AsyncCallback<List<AccountDTO>>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(List<AccountDTO> result) {
                    currentList.clear();
                    for(AccountDTO a : result) {
                        ClientDTO c = a.getOwner();
                        boolean ownsAccount = sessionManager.isAdmin() || c.getEmail().equals(sessionManager.getUsername());
                        if(ownsAccount || currentList==accountsList[DST]) {
                            String cName = c.getName() + " " + c.getLastName();
                            String item = String.valueOf(a.getId()) + " (" + cName + ")";
                            if(ownsAccount)
                                item += " - Balance: " + a.getBalance();
                            currentList.addItem(item, String.valueOf(a.getId()));
                        }
                    }
                }
            };
        }
        
        final AsyncCallback<TransactionDTO> confirmTransactionCallbak = new AsyncCallback<TransactionDTO>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(TransactionDTO result) {
                updateListedAccounts();
            }
        };
        
        confirmTransactionButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showConfirmBox(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        try {
                            int srcSelectedIdx = accountsList[SRC].getSelectedIndex();
                            int dstSelectedIdx = accountsList[DST].getSelectedIndex();
                            Long srcId = Long.valueOf(accountsList[SRC].getValue(srcSelectedIdx));
                            Long dstId = Long.valueOf(accountsList[DST].getValue(dstSelectedIdx));
                            double amount = Double.valueOf(amountBox.getText());
                            getService().makeTransaction(srcId, dstId, amount, confirmTransactionCallbak);
                        } catch(IndexOutOfBoundsException e) {
                        }
                        popupWidget.hide();
                    }
                });   
            }
        });
        
        vPanel.add(pageTitle);
        final HorizontalPanel pageHPanel = new HorizontalPanel();
        final HorizontalPanel[] hPanel = new HorizontalPanel[2];
        for(int i=0; i<2; i++) {
            hPanel[i] = new HorizontalPanel();
            hPanel[i].add(searchCriteriaList[i]);
            hPanel[i].add(filterButton[i]);
        }
        
        sourceGrid = new Grid(4,2);
        sourceGrid.setWidget(0, 0, sourceTitle);
        sourceGrid.setWidget(1, 0, searchBox[0]);
        sourceGrid.setWidget(1, 1, hPanel[0]);
        sourceGrid.setWidget(2, 0, new Label("Account: "));
        sourceGrid.setWidget(2, 1, accountsList[0]);
        sourceGrid.setWidget(3, 0, amountBox);
        sourceGrid.setWidget(3, 1, confirmTransactionButton);
        
        destinationGrid = new Grid(3,2);
        destinationGrid.setWidget(0, 0, destinationTitle);
        destinationGrid.setWidget(1, 0, searchBox[1]);
        destinationGrid.setWidget(1, 1, hPanel[1]);
        destinationGrid.setWidget(2, 0, new Label("Account: "));
        destinationGrid.setWidget(2, 1, accountsList[1]);
        
        pageHPanel.add(sourceGrid);
        pageHPanel.add(destinationGrid);
        vPanel.add(pageHPanel);
    }

    @Override
    public Widget getWidget() {
        findAllAccounts(SRC);
        findAllAccounts(DST);
        return vPanel;
    }
    
}
