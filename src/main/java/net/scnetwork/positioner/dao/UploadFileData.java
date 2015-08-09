package net.scnetwork.positioner.dao;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class UploadFileData implements Upload.Receiver, Upload.SucceededListener{
    private File file;

    @Override
    public OutputStream receiveUpload(String filename, String s1) {
        FileOutputStream fos = null;
        try {
            file = new File("/tmp/upload/" + filename);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Notification.show("Не удалось загрузить файл", Notification.Type.ERROR_MESSAGE);
        }
        return fos;
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {

    }
}
