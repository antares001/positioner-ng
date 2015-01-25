package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.controller.MenuSelectedEvents.AdminDevicesMenu;
import pmr.mvd.positioner.dao.SqlDao;

import java.util.ArrayList;
import java.util.HashMap;

public class AddNewDevice implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private AddDevice addDevice;
    private AdminDevicesMenu adminDevicesMenu;

    public AddNewDevice(AddDevice arg0, AdminDevicesMenu arg1){
        this.addDevice = arg0;
        this.adminDevicesMenu = arg1;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        TextField username = addDevice.getUsername();
        TextField idname = addDevice.getIdname();
        if (username.getValue().equals("")){
            Notification.show("Не введено имя транспортного средства");
        } else if (idname.getValue().equals("")){
            Notification.show("Не введен уникальный идентификатор");
        } else {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("name", username.getValue());
            params.put("id", idname.getValue());
            if (dao.ExecuteOperation(params, "add_new_device")) {
                Window window = addDevice.getWindow();
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
