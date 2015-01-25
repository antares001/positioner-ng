package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.controller.MenuSelectedEvents.AdminDevicesMenu;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class DeleteDevice implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private AdminDevicesMenu adminDevicesMenu;

    public DeleteDevice(AdminDevicesMenu arg){
        this.adminDevicesMenu = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        ArrayList<Devices> devices = dao.GetDevices();
        try {
            HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
            Devices d = devices.get(Integer.parseInt(hidden.pullUp("delete_device")) - 1);

            HashMap<String,String> params = new HashMap<String, String>();
            params.put("name", d.getName());

            if (dao.ExecuteOperation(params, "del_device")) {
                Notification.show("Удалено транс. средство: " + d.getName() + "");

                Button deleteDevice = adminDevicesMenu.getDeleteDevice();
                Button devGroupButton = adminDevicesMenu.getDevGroupButton();

                deleteDevice.setEnabled(false);
                devGroupButton.setEnabled(false);
            } else
                Notification.show("Ошибка удаления ТС");
        } catch (Exception e){
            Notification.show("ТС уже удалено");
        }
    }
}
