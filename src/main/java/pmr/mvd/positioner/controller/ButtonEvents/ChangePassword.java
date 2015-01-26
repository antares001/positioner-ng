package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.utils.HiddenVariable;

public class ChangePassword implements Button.ClickListener{
    private PasswordField chPassText;
    private PasswordField chRepeatText;
    private Window window;

    public PasswordField getChPassText(){
        return this.chPassText;
    }

    public void setChPassText(PasswordField arg){
        this.chPassText = arg;
    }

    public PasswordField getChRepeatText(){
        return this.chRepeatText;
    }

    public void setChRepeatText(PasswordField arg){
        this.chRepeatText = arg;
    }

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        setWindow(new Window("Смена пароля пользователя " + hidden.pullUp("selected_user")));
        window.setWidth(500.0f, Sizeable.Unit.PIXELS);
        window.setHeight(200.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        final FormLayout fLayout = new FormLayout();
        final CustomLayout chPass = new CustomLayout("changepass");

        setChPassText(new PasswordField());
        setChRepeatText(new PasswordField());
        chPass.addComponent(chPassText, "password");
        chPass.addComponent(chRepeatText, "repeat");

        final Button changePassButton = new Button("Сменить");
        changePassButton.addClickListener(new ChangePasswordSave(this));
        chPass.addComponent(changePassButton, "addbutton");

        final Button closeChangePass = new Button("Отмена", new CloseWindow(window));
        chPass.addComponent(closeChangePass, "close");

        fLayout.addComponent(chPass);
        window.setContent(fLayout);
        UI.getCurrent().addWindow(window);
    }
}
