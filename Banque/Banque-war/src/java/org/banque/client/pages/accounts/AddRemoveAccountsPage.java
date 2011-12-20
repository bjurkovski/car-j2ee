/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages.accounts;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.banque.client.pages.WebPage;

/**
 *
 * @author bjurkovski
 */
public class AddRemoveAccountsPage implements WebPage {
    private VerticalPanel vPanel;
    private Label pageTitle;
    private Label addTitle;
    private Label removeTitle;
    
    public AddRemoveAccountsPage() {
        vPanel = new VerticalPanel();
        pageTitle = new Label("Manage Accounts");
        addTitle = new Label("Add Account");
        removeTitle = new Label("Remove Account");
    }

    @Override
    public Widget getWidget() {
        return vPanel;
    }
    
}
