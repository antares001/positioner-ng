package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

public class DeleteUserConfirm implements Button.ClickListener {
    private SqlDao dao = new SqlDao();

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());

        final Window winDelUser = new Window("Удаление пользователя " + hidden.pullUp("selected_user"));
        winDelUser.setWidth(400.0f, Sizeable.Unit.PIXELS);
        winDelUser.setHeight(200.0f, Sizeable.Unit.PIXELS);
        winDelUser.setModal(true);

        FormLayout delLayout = new FormLayout();
        CustomLayout delCustom = new CustomLayout("delete");

        final Button delConfirm = new Button("Удалить", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (dao.DelUser(hidden.pullUp("selected_user")))
                    winDelUser.close();
                else
                    Notification.show("Ошибка удаления пользователя");
            }
        });
        delCustom.addComponent(delConfirm, "delete");

        final Button delClose = new Button("Отмена", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                winDelUser.close();
            }
        });
        delCustom.addComponent(delClose, "close");

        delLayout.addComponent(delCustom);
        winDelUser.setContent(delLayout);
        UI.getCurrent().addWindow(winDelUser);
    }
}