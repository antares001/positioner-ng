package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import pmr.mvd.positioner.utils.HiddenVariable;

public class ChangeGroup implements Button.ClickListener{
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());

        final Window winChGroup = new Window("Изменение группы для " + hidden.pullUp("selected_user"));
        winChGroup.setWidth(400.0f, Sizeable.Unit.PIXELS);
        winChGroup.setHeight(200.0f, Sizeable.Unit.PIXELS);
        winChGroup.setModal(true);

        FormLayout chLayout = new FormLayout();
        winChGroup.setContent(chLayout);
        UI.getCurrent().addWindow(winChGroup);
    }
}
