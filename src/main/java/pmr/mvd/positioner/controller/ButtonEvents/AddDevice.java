package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import pmr.mvd.positioner.dao.SqlDao;

import java.util.HashMap;

public class AddDevice implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    private Window window = new Window("Добавить");

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final FormLayout formLayout = new FormLayout();
        window.setWidth(400.0f, Sizeable.Unit.PIXELS);
        window.setHeight(200.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

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
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("name", username.getValue());
                    params.put("id", idname.getValue());
                    if (dao.ExecuteOperation(params, "add_new_device"))
                        window.close();
                    else
                        Notification.show("Ошибка добавления ТС");
                }
            }
        });
        layout.addComponent(add, "addbutton");

        Button close = new Button("Закрыть", new CloseWindow(window));
        layout.addComponent(close, "close");

        formLayout.addComponent(layout);

        window.setContent(formLayout);
        UI.getCurrent().addWindow(window);
    }
}
