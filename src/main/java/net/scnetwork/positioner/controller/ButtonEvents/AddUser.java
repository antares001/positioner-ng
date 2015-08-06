package net.scnetwork.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import net.scnetwork.positioner.controller.MenuSelectedEvents.AdminUsersMenu;

public class AddUser implements Button.ClickListener{
    private Window window;
    private TextField userName;
    private PasswordField passWord;
    private PasswordField repeatPass;
    private ComboBox comboBox;
    private AdminUsersMenu adminUsersMenu;

    public AddUser(AdminUsersMenu arg) {
        this.adminUsersMenu = arg;
    }

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    public ComboBox getComboBox(){
        return this.comboBox;
    }

    public void setComboBox(ComboBox arg){
        this.comboBox = arg;
    }

    public TextField getUserName(){
        return this.userName;
    }

    public void setUserName(TextField arg){
        this.userName = arg;
    }

    public PasswordField getPassWord(){
        return this.passWord;
    }

    public void setPassWord(PasswordField arg){
        this.passWord = arg;
    }

    public PasswordField getRepeatPass(){
        return this.repeatPass;
    }

    public void setRepeatPass(PasswordField arg){
        this.repeatPass = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        setWindow(new Window("Добавить"));
        window.setWidth(500.0f, Sizeable.Unit.PIXELS);
        window.setHeight(300.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        final FormLayout formLayout = new FormLayout();
        final CustomLayout customLayout = new CustomLayout("adduser");

        setUserName(new TextField());
        setPassWord(new PasswordField());
        setRepeatPass(new PasswordField());
        customLayout.addComponent(userName, "name");
        customLayout.addComponent(passWord, "password");
        customLayout.addComponent(repeatPass, "repeat");

        setComboBox(new ComboBox());
        comboBox.setImmediate(true);
        comboBox.setNullSelectionAllowed(false);

        comboBox.addItem("Пользователь");
        comboBox.addItem("Администратор");
        customLayout.addComponent(comboBox, "group");

        final Button addNewUser = new Button("Добавить");
        addNewUser.addClickListener(new AddNewUser(this, adminUsersMenu));
        customLayout.addComponent(addNewUser, "addbutton");

        final Button closeNewUser = new Button("Отмена", new CloseWindow(window));
        customLayout.addComponent(closeNewUser, "close");

        formLayout.addComponent(customLayout);
        window.setContent(formLayout);
        UI.getCurrent().addWindow(window);
    }
}
