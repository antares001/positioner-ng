package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.GroupDev;
import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.controller.MenuSelectedEvents.AdminDevicesMenu;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class DevGroup implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private ComboBox comboBoxUsers;
    private Table table;
    private Button del;
    private Window window;
    private Window winAddGroupUser = new Window("Новый пользователь");
    private AdminDevicesMenu adminDevicesMenu;

    public DevGroup(AdminDevicesMenu arg){
        this.adminDevicesMenu = arg;
    }

    public Button getDel(){
        return this.del;
    }

    public void setDel(Button arg){
        this.del = arg;
    }

    public Table getTable(){
        return this.table;
    }

    public void setTable(Table arg){
        this.table = arg;
    }

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    public ComboBox getComboBoxUsers(){
        return this.comboBoxUsers;
    }

    public void setComboBoxUsers(ComboBox arg){
        this.comboBoxUsers = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        setWindow(new Window("Список пользователей для данного ТС"));
        window.setWidth(600.0f, Sizeable.Unit.PIXELS);
        window.setHeight(400.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        FormLayout chDevLayaout = new FormLayout();

        VerticalLayout vDev = new VerticalLayout();

        table.setSelectable(true);

        table.setPageLength(5);
        table.setWidth(550.0f, Sizeable.Unit.PIXELS);

        table.addContainerProperty("Пользователь", String.class, null);

        ArrayList<Devices> devices = dao.GetDevices();
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        Devices d = devices.get(Integer.parseInt(hidden.pullUp("delete_device")) - 1);
        final String namedevice = d.getName();

        final ArrayList<GroupDev> userGroup = dao.GetGroupDev(namedevice);
        for (GroupDev groupDev : userGroup){
            try{
                String name = groupDev.getUser();

                Object newItem = table.addItem();
                Item row = table.getItem(newItem);
                row.getItemProperty("Пользователь").setValue(name);
            }catch (NullPointerException ignored){}
        }

        vDev.addComponent(table);

        CustomLayout customDevGroup = new CustomLayout("usergroup");

        final Button add = new Button("Добавить");
        add.addClickListener(new Add());
        customDevGroup.addComponent(add, "add");

        setDel(new Button("Удалить"));
        del.addClickListener(new Del());
        del.setEnabled(false);
        customDevGroup.addComponent(del, "delete");

        setTable(new Table("Список пользователей"));
        table.addValueChangeListener(new UsersTableListener());

        final Button close = new Button("Закрыть", new CloseWindow(window));
        customDevGroup.addComponent(close, "exit");
        vDev.addComponent(customDevGroup);

        chDevLayaout.addComponent(vDev);
        window.setContent(chDevLayaout);
        UI.getCurrent().addWindow(window);
    }

    private class UsersTableListener implements Property.ValueChangeListener{
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
            del.setEnabled(true);
            hidden.pullDown("delete_groupuser", String.valueOf(valueChangeEvent.getProperty().getValue()));
        }
    }

    private class Del implements Button.ClickListener{
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

    public class Add implements Button.ClickListener{
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            winAddGroupUser.setModal(true);
            winAddGroupUser.setWidth(400.0f, Sizeable.Unit.PIXELS);
            winAddGroupUser.setHeight(200.0f, Sizeable.Unit.PIXELS);

            FormLayout addGroupUserLayout = new FormLayout();
            CustomLayout layout0 = new CustomLayout("changegroup");

            setComboBoxUsers(new ComboBox());
            comboBoxUsers.setWidth(200.0f, Sizeable.Unit.PIXELS);
            comboBoxUsers.setImmediate(true);
            comboBoxUsers.setNullSelectionAllowed(false);

            final ArrayList<UserSettings> listUsers = dao.GetUsers();
            for (UserSettings us : listUsers){
                comboBoxUsers.addItem(us.getUsername());
            }
            layout0.addComponent(comboBoxUsers, "group");

            final Button saveGroupUser = new Button("Добавить");
            saveGroupUser.addClickListener(new SaveGroupUser());
            layout0.addComponent(saveGroupUser, "save");

            final Button closeGroupUser = new Button("Отмена", new CloseWindow(winAddGroupUser));
            layout0.addComponent(closeGroupUser, "close");

            addGroupUserLayout.addComponent(layout0);

            winAddGroupUser.setContent(addGroupUserLayout);
            UI.getCurrent().addWindow(winAddGroupUser);
        }
    }

    public class SaveGroupUser implements Button.ClickListener{
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
            ArrayList<Devices> devices = dao.GetDevices();
            Devices d = devices.get(Integer.parseInt(hidden.pullUp("delete_device")) - 1);
            final String namedevice = d.getName();

            ArrayList<GroupDev> userGroup = dao.GetGroupDev(namedevice);
            try{
                String nameuser = comboBoxUsers.getValue().toString();
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
                        winAddGroupUser.close();

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
}
