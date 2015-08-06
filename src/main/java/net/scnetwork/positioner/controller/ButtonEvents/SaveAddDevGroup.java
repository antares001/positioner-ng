package net.scnetwork.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import net.scnetwork.positioner.bean.Devices;
import net.scnetwork.positioner.bean.GroupDev;
import net.scnetwork.positioner.dao.SqlDao;
import net.scnetwork.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveAddDevGroup implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private AddDevGroup addDevGroup;
    private DevGroup devGroup;

    public SaveAddDevGroup(AddDevGroup arg0, DevGroup arg1){
        this.addDevGroup = arg0;
        this.devGroup = arg1;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        ArrayList<Devices> devices = dao.GetDevices();
        Devices d = devices.get(Integer.parseInt(hidden.pullUp("delete_device")) - 1);
        final String namedevice = d.getName();

        ArrayList<GroupDev> userGroup = dao.GetGroupDev(namedevice);
        try{
            ComboBox comboBox = addDevGroup.getComboBoxUsers();
            String nameuser = comboBox.getValue().toString();
            boolean newUser = true;
            for(GroupDev settingsUser : userGroup){
                if (nameuser.equals(settingsUser.getUser()))
                    newUser = false;
            }
            if (newUser) {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put("user", nameuser);
                params.put("device", namedevice);

                if (dao.ExecuteOperation(params, "add_group_user")) {
                    Table table = devGroup.getTable();
                    Window window = addDevGroup.getWindow();

                    window.close();

                    table.removeAllItems();
                    userGroup = dao.GetGroupDev(namedevice);
                    for (GroupDev groupDev : userGroup){
                        try{
                            String name = groupDev.getUser();

                            Object newItem = table.addItem();
                            Item row = table.getItem(newItem);
                            row.getItemProperty("Пользователь").setValue(name);
                        }catch (NullPointerException ignored){}
                    }
                }else
                    Notification.show("Ошибка добавления пользователя");
            } else
                Notification.show("Такой пользователь уже есть");
        } catch (NullPointerException e){
            Notification.show("Не выбран пользователь");
        }
    }
}
