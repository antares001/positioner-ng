package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.GroupDev;
import pmr.mvd.positioner.controller.TableChangeListener.UserTableListener;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;

public class DevGroup implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private Table table;
    private Button del;
    private Window window;

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

    /**
     * Обработка нажатия на Администрирование->Устройства->кнопка "Пользователи"
     * @param clickEvent событие
     */
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        setWindow(new Window("Список пользователей для данного ТС"));
        window.setWidth(600.0f, Sizeable.Unit.PIXELS);
        window.setHeight(400.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        FormLayout chDevLayaout = new FormLayout();

        VerticalLayout vDev = new VerticalLayout();

        setTable(new Table("Список пользователей"));
        table.addValueChangeListener(new UserTableListener(this));
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
        add.addClickListener(new AddDevGroup(this));
        customDevGroup.addComponent(add, "add");

        setDel(new Button("Удалить"));
        del.addClickListener(new DelDevGroup(this));
        del.setEnabled(false);
        customDevGroup.addComponent(del, "delete");

        final Button close = new Button("Закрыть", new CloseWindow(window));
        customDevGroup.addComponent(close, "exit");
        vDev.addComponent(customDevGroup);

        chDevLayaout.addComponent(vDev);
        window.setContent(chDevLayaout);
        UI.getCurrent().addWindow(window);
    }
}
