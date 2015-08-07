package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import net.scnetwork.positioner.controller.MainView;
import net.scnetwork.positioner.utils.HiddenVariable;

public class ConnectDatabases implements MenuBar.Command{
    private MainView mainView;
    private Window window;

    public ConnectDatabases(MainView arg){
        this.mainView = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        window = new Window("Подключение к базе");
        window.setModal(true);
        window.setHeight(300.0f, Sizeable.Unit.PIXELS);
        window.setWidth(800.0f, Sizeable.Unit.PIXELS);

        UI.getCurrent().addWindow(window);
    }
}
