package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.GroupDev;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class DelDevGroup implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private DevGroup devGroup;

    public DelDevGroup(DevGroup arg){
        this.devGroup = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());

        ArrayList<Devices> devices = dao.GetDevices();
        Devices d = devices.get(Integer.parseInt(hidden.pullUp("delete_device")) - 1);
        final String namedevice = d.getName();

        final ArrayList<GroupDev> userGroup = dao.GetGroupDev(namedevice);

        String uName = hidden.pullUp("delete_groupuser");
        String nmas = userGroup.get(Integer.parseInt(uName) - 1).getUser();

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("user", nmas);
        params.put("device", namedevice);

        if (dao.ExecuteOperation(params, "del_group_dev")){
            Table table = devGroup.getTable();
            table.removeAllItems();
            for (GroupDev groupDev : dao.GetGroupDev(namedevice)){
                try{
                    String name = groupDev.getUser();

                    Object newItem = table.addItem();
                    Item row = table.getItem(newItem);
                    row.getItemProperty("Пользователь").setValue(name);
                }catch (NullPointerException ignored){}
            }
        } else {
            Notification.show("Ошибка удаления пользователя");
        }
    }
}
