package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.HashMap;

public class DeleteUserConfirm implements Button.ClickListener {
    private SqlDao dao = new SqlDao();

    private HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
    private Window window = new Window("Удаление пользователя " + hidden.pullUp("selected_user"));

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        window.setWidth(400.0f, Sizeable.Unit.PIXELS);
        window.setHeight(200.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        FormLayout delLayout = new FormLayout();
        CustomLayout delCustom = new CustomLayout("delete");

        final Button delConfirm = new Button("Удалить");
        delConfirm.addClickListener(new Del());
        delCustom.addComponent(delConfirm, "delete");

        final Button delClose = new Button("Отмена", new CloseWindow(window));
        delCustom.addComponent(delClose, "close");

        delLayout.addComponent(delCustom);
        window.setContent(delLayout);
        UI.getCurrent().addWindow(window);
    }

    private class Del implements Button.ClickListener{
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("user", hidden.pullUp("selected_user"));

            if (dao.ExecuteOperation(params, "delete_user")) {
                window.close();
            } else
                Notification.show("Ошибка удаления пользователя");
        }
    }
}