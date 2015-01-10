package pmr.mvd.positioner.controller;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class Settings extends CustomComponent implements View, Button.ClickListener{
    public static final String NAME = "settings";

    public Settings(){
        setSizeFull();

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Panel panel = new Panel("Настройки системы");
        panel.setSizeUndefined();
        layout.addComponent(panel);
        layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        final Button back = new Button("Отмена", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().getNavigator().navigateTo(MainView.NAME);
            }
        });
        layout.addComponent(back);

        setCompositionRoot(layout);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        //
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //
    }
}
