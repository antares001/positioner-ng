package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.GroupDev;
import pmr.mvd.positioner.controller.MenuSelectedEvents.AdminDevicesMenu;
import pmr.mvd.positioner.controller.TableChangeListener.DevTableListener;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupListDevices implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    private Window window = new Window("Список транспортных средств пользователя");
    private Button del;
    private Table tabDevGroup;

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    public Table getTabDevGroup(){
        return this.tabDevGroup;
    }

    public void setTabDevGroup(Table arg){
        this.tabDevGroup = arg;
    }

    public Button getDel(){
        return this.del;
    }

    public void setDel(Button arg){
        this.del = arg;
    }

    /**
     * Оработка нажатия кнопки Администрирование->Пользователи->ТС
     * @param clickEvent событие
     */
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        window.setWidth(600.0f, Sizeable.Unit.PIXELS);
        window.setHeight(400.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        FormLayout chDevLayaout = new FormLayout();

        VerticalLayout vDev = new VerticalLayout();

        setTabDevGroup(new Table("Транспортные средства"));
        tabDevGroup.setSelectable(true);

        tabDevGroup.setPageLength(5);
        tabDevGroup.setWidth(550.0f, Sizeable.Unit.PIXELS);

        tabDevGroup.addContainerProperty("Транспортное средство", String.class, null);

        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        String nameuser = hidden.pullUp("selected_user");
        ArrayList<GroupDev> devGroup = dao.GetGroupUser(nameuser);
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

        final Button add = new Button("Добавить");
        add.addClickListener(new AddGroupListDevices(this));
        customDevGroup.addComponent(add, "add");

        setDel(new Button("Удалить"));
        del.addClickListener(new DelGroupListDevices(this));
        del.setEnabled(false);
        customDevGroup.addComponent(del, "delete");

        tabDevGroup.addValueChangeListener(new DevTableListener(this));

        final Button close = new Button("Закрыть", new CloseWindow(window));
        customDevGroup.addComponent(close, "exit");
        vDev.addComponent(customDevGroup);

        chDevLayaout.addComponent(vDev);
        window.setContent(chDevLayaout);
        UI.getCurrent().addWindow(window);
    }
}
