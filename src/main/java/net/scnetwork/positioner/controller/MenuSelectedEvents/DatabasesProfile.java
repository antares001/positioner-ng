package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;

public class DatabasesProfile implements MenuBar.Command{
    private Window window;

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        setWindow(new Window("Управление транспортными средствами"));
        window.setWidth(600.0f, Sizeable.Unit.PIXELS);
        window.setHeight(400.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);
        final FormLayout formLayout = new FormLayout();

        window.setContent(formLayout);
        UI.getCurrent().addWindow(window);
    }
}
