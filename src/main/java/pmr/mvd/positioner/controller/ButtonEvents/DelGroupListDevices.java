package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import pmr.mvd.positioner.bean.GroupDev;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class DelGroupListDevices implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private GroupListDevices groupListDevices;

    public DelGroupListDevices(GroupListDevices arg){
        this.groupListDevices = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        String nameuser = hidden.pullUp("selected_user");
        ArrayList<GroupDev> devGroup = dao.GetGroupUser(nameuser);

        String mDev = hidden.pullUp("delete_groupdevice");
        String nn = devGroup.get(Integer.parseInt(mDev) - 1).getDevice();

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("user", nameuser);
        params.put("device", nn);

        if (dao.ExecuteOperation(params, "del_group_dev")){
            Window window = groupListDevices.getWindow();
            Table tabDevGroup = groupListDevices.getTabDevGroup();
            Button del = groupListDevices.getDel();
            window.close();

            for (GroupDev groupDev : devGroup){
                try {
                    String name = groupDev.getDevice();

                    Object newItem = tabDevGroup.addItem();
                    Item row = tabDevGroup.getItem(newItem);
                    row.getItemProperty("Транспортное средство").setValue(name);
                    del.setEnabled(false);
                } catch (NullPointerException ignored){}
            }
        } else {
            Notification.show("Ошибка удаления транспортного средства");
        }
    }
}
