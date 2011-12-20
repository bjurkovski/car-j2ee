/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.ui;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author bjurkovski
 */
public class PopupWidget extends DialogBox {
    private Button okButton = null;
    private Button cancelButton = null;
    private Widget content;
    private HorizontalPanel buttonsPanel;
    private Grid grid;
    
    public PopupWidget(String title) {
        super();
        createPopupWidget(title, true);
    }
    
    public PopupWidget(String title, boolean canCancel) {
        super();
        createPopupWidget(title, canCancel);
    }
    
    private void createPopupWidget(String title, boolean canCancel) {
        setText(title);
        grid = new Grid(2, 1);
        
        buttonsPanel = new HorizontalPanel();
        buttonsPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        okButton = new Button("OK");
        buttonsPanel.add(okButton);
        this.canCancel(canCancel);
        
        grid.setWidget(1, 0, buttonsPanel);
        setWidget(grid);
    }
    
    public void setContent(Widget c) {
        grid.setWidget(0, 0, c);
    }
    
    public void addOKHandler(ClickHandler handler) {
        okButton.addClickHandler(handler);
    }
    
    public void addCancelHandler(ClickHandler handler) {
        if(cancelButton != null) {
            cancelButton.addClickHandler(handler);
        }
    }
    
    public void canCancel(boolean value) {
        if(value && (cancelButton == null)) {
            cancelButton = new Button("Cancel");
            buttonsPanel.add(cancelButton);
        }
        else if((cancelButton != null) && !value) {
            buttonsPanel.remove(cancelButton);
            cancelButton = null;
        }
    }
}
