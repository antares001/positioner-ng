package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import net.scnetwork.positioner.controller.MainView;

public class LoadFile implements MenuBar.Command{
    private MainView mainView;
    private Window window;

    public LoadFile(MainView arg){
        this.mainView = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        window = new Window("Загрузить файл");
        window.setModal(true);
        window.setHeight(300.0f, Sizeable.Unit.PIXELS);
        window.setWidth(800.0f, Sizeable.Unit.PIXELS);
        UI.getCurrent().addWindow(window);
    }
}
