package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import pmr.mvd.positioner.controller.MenuSelectedEvents.AdminDevicesMenu;

public class AddDevice implements Button.ClickListener{
    private Window window;
    private TextField username;
    private TextField idname;
    private AdminDevicesMenu adminDevicesMenu;

    public AddDevice(AdminDevicesMenu arg){
        this.adminDevicesMenu = arg;
    }

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    public TextField getUsername(){
        return this.username;
    }

    public void setUsername(TextField arg){
        this.username = arg;
    }

    public TextField getIdname(){
        return this.idname;
    }

    public void setIdname(TextField arg){
        this.idname = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final FormLayout formLayout = new FormLayout();

        setWindow(new Window("Добавить"));
        window.setWidth(400.0f, Sizeable.Unit.PIXELS);
        window.setHeight(200.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        final CustomLayout layout = new CustomLayout("newuser");

        setUsername(new TextField());
        layout.addComponent(username, "nameInput");

        setIdname(new TextField());
        layout.addComponent(idname, "idInput");

        final Button add = new Button("Добавить");
        add.addClickListener(new AddNewDevice(this, adminDevicesMenu));
        layout.addComponent(add, "addbutton");

        Button close = new Button("Закрыть", new CloseWindow(window));
        layout.addComponent(close, "close");

        formLayout.addComponent(layout);

        window.setContent(formLayout);
        UI.getCurrent().addWindow(window);
    }
}
