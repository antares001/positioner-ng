package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class PrintGroup implements MenuBar.Command {
    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        final Window windowPrintGroup = new Window("Отчет для всей группы ТС");
        windowPrintGroup.setWidth(800.0f, Sizeable.Unit.PIXELS);
        windowPrintGroup.setModal(true);
        final FormLayout formLayout = new FormLayout();
        windowPrintGroup.setContent(formLayout);
        UI.getCurrent().addWindow(windowPrintGroup);
    }
}
