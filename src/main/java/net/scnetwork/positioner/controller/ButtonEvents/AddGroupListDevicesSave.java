package net.scnetwork.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import net.scnetwork.positioner.bean.GroupDev;
import net.scnetwork.positioner.dao.SqlDao;
import net.scnetwork.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class AddGroupListDevicesSave implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private AddGroupListDevices addGroupListDevices;
    private GroupListDevices groupListDevices;

    public AddGroupListDevicesSave(AddGroupListDevices arg0, GroupListDevices arg1){
        this.addGroupListDevices = arg0;
        this.groupListDevices = arg1;
    }

    /**
     * Администрирование->Управление пользователями->ТС->Добавить->Сохранить
     * @param clickEvent событие
     */
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        String nameuser = hidden.pullUp("selected_user");
        ArrayList<GroupDev> devGroup = dao.GetGroupUser(nameuser);
        try {
            ComboBox comboBox = addGroupListDevices.getComboBox();
            String namedevice = comboBox.getValue().toString();
            boolean newDevice = true;
            for (GroupDev gd : devGroup) {
                if (namedevice.equals(gd.getDevice()))
                    newDevice = false;
            }

            if (newDevice) {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put("user", nameuser);
                params.put("device", namedevice);

                if (dao.ExecuteOperation(params, "add_group_user")) {
                    Window window = addGroupListDevices.getWindow();
                    window.close();
                    Table tabDevGroup = groupListDevices.getTabDevGroup();
                    tabDevGroup.removeAllItems();

                    for (GroupDev groupDev : devGroup){
                        try {
                            String name = groupDev.getDevice();

                            Object newItem = tabDevGroup.addItem();
                            Item row = tabDevGroup.getItem(newItem);
                            row.getItemProperty("Транспортное средство").setValue(name);
                        } catch (NullPointerException ignored){}
                    }
                } else
                    Notification.show("Ошибка добавления ТС");
            } else {
                Notification.show("Такое ТС уже есть");
            }
        } catch (NullPointerException e){
            Notification.show("Не выбрано ТС");
        }
    }
}
