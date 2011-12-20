/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages.accounts;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.banque.client.pages.WebPage;

/**
 *
 * @author bjurkovski
 */
public class SearchAccountsPage implements WebPage {
    private VerticalPanel vPanel;
    
    public SearchAccountsPage() {
        
    }

    @Override
    public Widget getWidget() {
        return vPanel;
    }
    
}
