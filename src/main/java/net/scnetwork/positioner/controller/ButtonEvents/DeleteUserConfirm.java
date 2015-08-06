package net.scnetwork.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import net.scnetwork.positioner.utils.HiddenVariable;

public class DeleteUserConfirm implements Button.ClickListener {
    private Window window;

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        setWindow(new Window("Удаление пользователя " + hidden.pullUp("selected_user")));
        window.setWidth(400.0f, Sizeable.Unit.PIXELS);
        window.setHeight(200.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        FormLayout delLayout = new FormLayout();
        CustomLayout delCustom = new CustomLayout("delete");

        final Button delConfirm = new Button("Удалить");
        delConfirm.addClickListener(new DeleteUserConfirmDel(this));
        delCustom.addComponent(delConfirm, "delete");

        final Button delClose = new Button("Отмена", new CloseWindow(window));
        delCustom.addComponent(delClose, "close");

        delLayout.addComponent(delCustom);
        window.setContent(delLayout);
        UI.getCurrent().addWindow(window);
    }
}