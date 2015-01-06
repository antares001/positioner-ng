package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import pmr.mvd.positioner.dao.SqlDao;

public class AddDevice implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final Window addTs = new Window("Добавить");
        final FormLayout formLayout = new FormLayout();
        addTs.setWidth(400.0f, Sizeable.Unit.PIXELS);
        addTs.setHeight(200.0f, Sizeable.Unit.PIXELS);
        addTs.setModal(true);

        final CustomLayout layout = new CustomLayout("newuser");

        final TextField username = new TextField();
        layout.addComponent(username, "nameInput");

        final TextField idname = new TextField();
        layout.addComponent(idname, "idInput");

        final Button add = new Button("Добавить", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (username.getValue().equals("")){
                    Notification.show("Не введено имя транспортного средства");
                } else if (idname.getValue().equals("")){
                    Notification.show("Не введен уникальный идентификатор");
                } else {
                    if(dao.AddNewDevice(username.getValue(), idname.getValue()))
                        addTs.close();
                    else
                        Notification.show("Ошибка добавления ТС");
                }
            }
        });
        layout.addComponent(add, "addbutton");

        Button close = new Button("Закрыть", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addTs.close();
            }
        });
        layout.addComponent(close, "close");

        formLayout.addComponent(layout);

        addTs.setContent(formLayout);
        UI.getCurrent().addWindow(addTs);
    }
}
