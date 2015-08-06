package net.scnetwork.positioner.controller.ButtonEvents;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

public class CloseWindow implements Button.ClickListener{
    private Window window;

    public CloseWindow(Window arg){
        this.window = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        window.close();
    }
}
