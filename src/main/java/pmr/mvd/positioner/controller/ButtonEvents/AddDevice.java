package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.controller.MenuSelectedEvents.AdminDevicesMenu;
import pmr.mvd.positioner.dao.SqlDao;

import java.util.ArrayList;
import java.util.HashMap;

public class AddDevice implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    private Window window = new Window("Добавить");
    private TextField username = new TextField();
    private TextField idname = new TextField();
    private AdminDevicesMenu adminDevicesMenu;

    public AddDevice(AdminDevicesMenu arg){
        this.adminDevicesMenu = arg;
    }

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
        layout.addComponent(username, "nameInput");
        layout.addComponent(idname, "idInput");

        final Button add = new Button("Добавить");
        add.addClickListener(new AddNewDevice());
        layout.addComponent(add, "addbutton");

        Button close = new Button("Закрыть", new CloseWindow(window));
        layout.addComponent(close, "close");

        formLayout.addComponent(layout);

        window.setContent(formLayout);
        UI.getCurrent().addWindow(window);
    }

    private class AddNewDevice implements Button.ClickListener{
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
                if (dao.ExecuteOperation(params, "add_new_device")) {
                    window.close();
                    Table table = adminDevicesMenu.getTabDevice();
                    table.removeAllItems();

                    ArrayList<Devices> devices = dao.GetDevices();
                    for (Devices device : devices){
                        try {
                            String id = device.getId();
                            String name = device.getName();
                            String positions = device.getUniq();

                            Object newItem = table.addItem();
                            Item row = table.getItem(newItem);
                            row.getItemProperty("id").setValue(id);
                            row.getItemProperty("Имя").setValue(name);
                            row.getItemProperty("Уникальный идентификатор").setValue(positions);
                        }catch (NullPointerException ignored){}
                    }
                } else
                    Notification.show("Ошибка добавления ТС");
            }
        }
    }
}
