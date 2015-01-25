package pmr.mvd.positioner.controller;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

        final Button fileConf = new Button("Конфигурация", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
                String fileSrc = basepath + "/VAADIN/connection.conf";
                Properties properties = new Properties();
                try {
                    InputStream inputStream = new FileInputStream(fileSrc);
                    properties.load(inputStream);

                    /** Проверка чтения файла конфигурации */
                    System.out.println(properties.getProperty("driver"));
                    System.out.println(properties.getProperty("hostname"));
                    System.out.println(properties.getProperty("username"));
                    System.out.println(properties.getProperty("password"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        layout.addComponent(fileConf);

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
