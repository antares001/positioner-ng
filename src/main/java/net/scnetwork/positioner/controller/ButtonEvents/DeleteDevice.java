package net.scnetwork.positioner.controller.ButtonEvents;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import net.scnetwork.positioner.bean.Devices;
import net.scnetwork.positioner.controller.MenuSelectedEvents.AdminDevicesMenu;
import net.scnetwork.positioner.dao.SqlDao;
import net.scnetwork.positioner.utils.HiddenVariable;

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
