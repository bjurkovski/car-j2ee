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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Iterator;
import java.util.List;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.SessionManager;
import org.banque.client.pages.WebPage;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.ClientDTO;
import org.banque.managers.interfaces.IAccountManagerLocal;


/**
 *
 * @author bjurkovski
 */
public class SearchAccountsPage extends WebPage {
    private VerticalPanel vPanel;
    private Label pageTitle;
    private TextBox searchBox;
    private ListBox categoriesList;
    private Button searchButton;
    private Button listAllButton;
    private VerticalPanel resultsPanel;
    private AsyncCallback<List<AccountDTO>> updateSearchResultsCallback;
    
    private void search() {
        resultsPanel.clear();
        final Label l = new Label("Search Results:");
        l.setStyleName("title");
        resultsPanel.add(l);
        int selectedCategory = categoriesList.getSelectedIndex();
        int sc = Integer.valueOf(categoriesList.getValue(selectedCategory));
        getService().findAccountsByCriteria(searchBox.getText(), sc, updateSearchResultsCallback);
    }
    
    private void findAll() {
        resultsPanel.clear();
        final Label l = new Label("All Accounts:");
        l.setStyleName("title");
        resultsPanel.add(l);
        getService().findAllAccounts(updateSearchResultsCallback);
    }
    
    public static BanqueServiceAsync getService() {
        return GWT.create(BanqueService.class);
    }
    
    public SearchAccountsPage() {
        setupPage();
    }
    
    public void setupPage() {
        vPanel = new VerticalPanel();
        pageTitle = new Label("Search Accounts");
        pageTitle.setStyleName("title");
        searchBox = new TextBox();
        categoriesList = new ListBox();
        searchButton = new Button("Search");
        listAllButton = new Button("List All");
        resultsPanel = new VerticalPanel();
        
        categoriesList.addItem("Negative Balance", String.valueOf(IAccountManagerLocal.BALANCE_NEGATIVE));
        categoriesList.addItem("Positive Balance", String.valueOf(IAccountManagerLocal.BALANCE_POSITIVE));
        if(sessionManager.isAdmin()) {
            categoriesList.addItem("Client Name", String.valueOf(IAccountManagerLocal.PRENOM_CLIENT));
            categoriesList.addItem("Client Last Name", String.valueOf(IAccountManagerLocal.NOM_CLIENT));
        }
        categoriesList.addItem("ID", String.valueOf(IAccountManagerLocal.ID));
        
        final HorizontalPanel searchPanel = new HorizontalPanel();
        searchPanel.add(searchBox);
        searchPanel.add(categoriesList);
        searchPanel.add(searchButton);
        searchPanel.add(listAllButton);
        
        vPanel.add(pageTitle);
        vPanel.add(searchPanel);
        vPanel.add(resultsPanel);
        
        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                search();
            }
        });
        
        listAllButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                findAll();
            }
        });
        
        updateSearchResultsCallback = new AsyncCallback<List<AccountDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                resultsPanel.add(new Label("A problem ocurred."));
            }

            @Override
            public void onSuccess(List<AccountDTO> result) {
                for(Iterator<AccountDTO> i = result.iterator(); i.hasNext(); ) {
                    AccountDTO a = i.next();
                    ClientDTO c = a.getOwner();
                    String usr = sessionManager.getUsername();
                    if(sessionManager.isAdmin() || c.getEmail().equals(usr)) {
                        String fullName = c.getName() + " " + c.getLastName();
                        String aId = String.valueOf(a.getId());
                        resultsPanel.add(new Label(aId + " (" + fullName + ") - Balance: " + a.getBalance()));
                    }
                }
            }
        };
    }

    @Override
    public Widget getWidget() {
        return vPanel;
    }
    
}
