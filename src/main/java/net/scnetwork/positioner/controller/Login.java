package net.scnetwork.positioner.controller;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import net.scnetwork.positioner.utils.HiddenVariable;

public class Login extends CustomComponent implements View, Button.ClickListener{
    public static final String NAME = "login";

    private final TextField user = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button loginButton = new Button("Войти", this);

    public Login() {
        setSizeFull();

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Panel panel = new Panel("Вход в систему");
        panel.setSizeUndefined();
        layout.addComponent(panel);
        layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        CustomLayout custom = new CustomLayout("loginlayout");
        custom.addStyleName("customlayoutexample");

        panel.setContent(custom);

        custom.addComponent(this.user, "username");
        custom.addComponent(this.password, "password");
        custom.addComponent(this.loginButton, "okbutton");

        setCompositionRoot(layout);
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        user.focus();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (!user.isValid() || !password.isValid()) {
            return;
        }

        String username = user.getValue();
        String pass = password.getValue();

        try {
            if (username.equals("admin") && pass.equals("admin")) {
                HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
                hidden.pullDown("username", username);
                hidden.pullDown("admin", "1");
                getSession().setAttribute("user", username);
                getUI().getNavigator().navigateTo(MainView.NAME);
            } else {
                password.focus();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
            Notification.show("Нет соеднинения с БД.");
        }
    }
}
