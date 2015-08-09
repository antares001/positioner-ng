package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import net.scnetwork.positioner.controller.MainView;
import net.scnetwork.positioner.dao.UploadFileData;

public class LoadFile implements MenuBar.Command{
    private MainView mainView;
    private Window window;

    public LoadFile(MainView arg){
        this.mainView = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        window = new Window("Загрузить файл");
        window.setModal(true);
        window.setHeight(300.0f, Sizeable.Unit.PIXELS);
        window.setWidth(800.0f, Sizeable.Unit.PIXELS);
        final FormLayout layout = new FormLayout();

        UploadFileData upload = new UploadFileData();
        Upload up = new Upload("Загрузка файлов", upload);
        up.setButtonCaption("Загрузка трека");
        up.addSucceededListener(upload);
        layout.addComponent(up);

        window.setContent(layout);
        UI.getCurrent().addWindow(window);
    }
}
