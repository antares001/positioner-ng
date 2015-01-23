package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.GroupDev;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupListDevices implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    private Window window = new Window("Список транспортных средств пользователя");
    private Button del = new Button("Удалить");
    private HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
    private String nameuser = hidden.pullUp("selected_user");
    private ArrayList<GroupDev> devGroup = dao.GetGroupUser(nameuser);
    private Table tabDevGroup = new Table("Транспортные средства");

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        window.setWidth(600.0f, Sizeable.Unit.PIXELS);
        window.setHeight(400.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        FormLayout chDevLayaout = new FormLayout();

        VerticalLayout vDev = new VerticalLayout();

        tabDevGroup.setSelectable(true);

        tabDevGroup.setPageLength(5);
        tabDevGroup.setWidth(550.0f, Sizeable.Unit.PIXELS);

        tabDevGroup.addContainerProperty("Транспортное средство", String.class, null);
        for (GroupDev groupDev : devGroup){
            try {
                String name = groupDev.getDevice();

                Object newItem = tabDevGroup.addItem();
                Item row = tabDevGroup.getItem(newItem);
                row.getItemProperty("Транспортное средство").setValue(name);
            } catch (NullPointerException ignored){}
        }

        vDev.addComponent(tabDevGroup);

        CustomLayout customDevGroup = new CustomLayout("usergroup");

        final Button add = new Button("Добавить", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                final Window winTSUser = new Window("Добавление транспортного средства для пользователя");
                winTSUser.setModal(true);
                winTSUser.setWidth(400.0f, Sizeable.Unit.PIXELS);
                winTSUser.setHeight(200.0f, Sizeable.Unit.PIXELS);

                FormLayout flTsUsers = new FormLayout();
                CustomLayout clTsUsers = new CustomLayout("changegroup");

                final ComboBox comboBoxTsDev = new ComboBox();
                comboBoxTsDev.setWidth(200.0f, Sizeable.Unit.PIXELS);
                comboBoxTsDev.setImmediate(true);
                comboBoxTsDev.setNullSelectionAllowed(false);

                final ArrayList<Devices> tsDevices = dao.GetDevices();
                for(Devices ts : tsDevices){
                    comboBoxTsDev.addItem(ts.getName());
                }
                clTsUsers.addComponent(comboBoxTsDev, "group");

                final Button saveDevGroup = new Button("Добавить", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        try {
                            String namedevice = comboBoxTsDev.getValue().toString();
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
                                    winTSUser.close();
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
                });
                clTsUsers.addComponent(saveDevGroup, "save");

                final Button closeDevGroup = new Button("Отмена", new CloseWindow(winTSUser));
                clTsUsers.addComponent(closeDevGroup, "close");

                flTsUsers.addComponent(clTsUsers);
                winTSUser.setContent(flTsUsers);

                UI.getCurrent().addWindow(winTSUser);
            }
        });
        customDevGroup.addComponent(add, "add");

        del.addClickListener(new Delete());
        del.setEnabled(false);
        customDevGroup.addComponent(del, "delete");

        tabDevGroup.addValueChangeListener(new DevTableListener());

        final Button close = new Button("Закрыть", new CloseWindow(window));
        customDevGroup.addComponent(close, "exit");
        vDev.addComponent(customDevGroup);

        chDevLayaout.addComponent(vDev);
        window.setContent(chDevLayaout);
        UI.getCurrent().addWindow(window);
    }

    private class DevTableListener implements Property.ValueChangeListener{
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            del.setEnabled(true);
            hidden.pullDown("delete_groupdevice", String.valueOf(valueChangeEvent.getProperty().getValue()));
        }
    }

    private class Delete implements Button.ClickListener{
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            String mDev = hidden.pullUp("delete_groupdevice");
            String nn = devGroup.get(Integer.parseInt(mDev) - 1).getDevice();

            HashMap<String,String> params = new HashMap<String, String>();
            params.put("user", nameuser);
            params.put("device", nn);

            if (dao.ExecuteOperation(params, "del_group_dev")){
                window.close();

                for (GroupDev groupDev : devGroup){
                    try {
                        String name = groupDev.getDevice();

                        Object newItem = tabDevGroup.addItem();
                        Item row = tabDevGroup.getItem(newItem);
                        row.getItemProperty("Транспортное средство").setValue(name);
                    } catch (NullPointerException ignored){}
                }
            } else {
                Notification.show("Ошибка удаления транспортного средства");
            }
        }
    }
}
