package pmr.mvd.positioner;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import pmr.mvd.positioner.controller.Login;
import pmr.mvd.positioner.controller.MainView;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "pmr.mvd.positioner.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        new Navigator(this, this);

        getNavigator().addView(Login.NAME, Login.class);
        getNavigator().addView(MainView.NAME, MainView.class);

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

        if (getSession().getAttribute("user") != null){
            getNavigator().navigateTo(Login.NAME);
            return;
        } else {
            getNavigator().navigateTo(MainView.NAME);
            return;
        }

    }

}
