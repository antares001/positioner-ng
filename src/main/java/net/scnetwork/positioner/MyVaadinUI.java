package net.scnetwork.positioner;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import net.scnetwork.positioner.controller.Login;
import net.scnetwork.positioner.controller.MainView;
import net.scnetwork.positioner.controller.Settings;


@Theme("MyTheme")
@SpringUI
public class MyVaadinUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        new Navigator(this, this);

        getNavigator().addView(Login.NAME, Login.class);
        getNavigator().addView(MainView.NAME, MainView.class);
        getNavigator().addView(Settings.NAME, Settings.class);

        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof Login;

                if (!isLoggedIn && !isLoginView) {
                    getNavigator().navigateTo(Login.NAME);
                    return false;
                } else if (isLoggedIn && isLoginView) {
                    //getNavigator().navigateTo(MainView.NAME);
                    return false;
                }
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }
        });

        if (getSession().getAttribute("user") != null) {
            getNavigator().navigateTo(Login.NAME);
            return;
        } else {
            getNavigator().navigateTo(MainView.NAME);
            return;
        }
    }

}
