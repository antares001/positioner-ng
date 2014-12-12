package pmr.mvd.positioner.controller;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.dao.SqlDao;

public class Login extends CustomComponent implements View, Button.ClickListener{
    public static final String NAME = "login";

    private SqlDao dao = new SqlDao();

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
            UserSettings settings = dao.GetUserSetting(username);
            if (pass.equals(settings.getPassword())) {
                getSession().setAttribute("user", username);
                getUI().getNavigator().navigateTo(MainView.NAME);
            } else {
                //password.setValue(null);
                password.focus();
            }
        } catch (NullPointerException e){
            Notification.show("Нет соеднинения с БД.");
        }
    }
}
