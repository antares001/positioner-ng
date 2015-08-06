package net.scnetwork.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import net.scnetwork.positioner.utils.HiddenVariable;

public class ChangeGroup implements Button.ClickListener{
    private ComboBox comboBox;
    private Window window;

    public ComboBox getComboBox(){
        return this.comboBox;
    }

    public void setComboBox(ComboBox arg){
        this.comboBox = arg;
    }

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());

        setWindow(new Window("Изменение группы для " + hidden.pullUp("selected_user")));
        window.setWidth(400.0f, Sizeable.Unit.PIXELS);
        window.setHeight(200.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        FormLayout chLayout = new FormLayout();
        CustomLayout layout = new CustomLayout("changegroup");

        setComboBox(new ComboBox());
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        comboBox.setNullSelectionAllowed(false);
        comboBox.setImmediate(true);

        comboBox.addItem("Пользователь");
        comboBox.addItem("Администратор");
        layout.addComponent(comboBox, "group");

        final Button save = new Button("Сменить");
        save.addClickListener(new ChangeGroupSave(this));
        layout.addComponent(save, "save");

        final Button close = new Button("Отмена", new CloseWindow(window));
        layout.addComponent(close, "close");

        chLayout.addComponent(layout);
        window.setContent(chLayout);
        UI.getCurrent().addWindow(window);
    }
}
