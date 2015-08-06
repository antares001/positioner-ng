package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.data.Property;
import com.vaadin.server.*;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import net.scnetwork.positioner.bean.Devices;
import net.scnetwork.positioner.dao.SqlDao;
import net.scnetwork.positioner.utils.PdfCreator;
import net.scnetwork.positioner.bean.Report;
import net.scnetwork.positioner.utils.HiddenVariable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PrintOne implements MenuBar.Command {
    private SqlDao dao = new SqlDao();
    private PdfCreator pdfCreator = new PdfCreator();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Window windowAddTs = new Window("Отчет для одного транспортного средства");
    private Button downloadReport;

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        windowAddTs.setWidth(750.0f, Sizeable.Unit.PIXELS);
        windowAddTs.setHeight(230.0f, Sizeable.Unit.PIXELS);
        windowAddTs.setModal(true);
        final FormLayout formLayout = new FormLayout();
        final CustomLayout customLayout = new CustomLayout("printone");

        final ComboBox comboBoxName = new ComboBox();
        comboBoxName.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        comboBoxName.setFilteringMode(FilteringMode.CONTAINS);
        comboBoxName.setImmediate(true);
        comboBoxName.setNullSelectionAllowed(false);

        ArrayList<Devices> dev = dao.GetDevices();
        for(Devices d : dev){
            comboBoxName.addItem(d.getName());
        }
        OneTsName oneTsName = new OneTsName();
        comboBoxName.addValueChangeListener(oneTsName);
        customLayout.addComponent(comboBoxName, "comboBoxName");

        final PopupDateField fromDate = new PopupDateField();
        fromDate.setValue(new Date());
        fromDate.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        fromDate.setImmediate(true);
        fromDate.setTimeZone(TimeZone.getTimeZone("Europe/Tiraspol"));
        fromDate.setLocale(Locale.forLanguageTag("RU"));
        fromDate.setResolution(Resolution.MINUTE);
        OneFirstDate firstDate = new OneFirstDate();
        fromDate.addValueChangeListener(firstDate);
        customLayout.addComponent(fromDate, "fromDate");

        final PopupDateField toDate = new PopupDateField();
        toDate.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        toDate.setValue(new Date());
        toDate.setImmediate(true);
        toDate.setTimeZone(TimeZone.getTimeZone("Europe/Tiraspol"));
        toDate.setLocale(Locale.forLanguageTag("RU"));
        toDate.setResolution(Resolution.MINUTE);
        OneLastDate lastDate = new OneLastDate();
        toDate.addValueChangeListener(lastDate);
        customLayout.addComponent(toDate, "toDate");

        final Button printReport = new Button("Сгенерировать");
        PrintOneReport printOneReport = new PrintOneReport();
        printReport.addClickListener(printOneReport);
        customLayout.addComponent(printReport, "printReport");

        downloadReport = new Button("Скачать отчет");
        downloadReport.setEnabled(false);
        customLayout.addComponent(downloadReport, "download");

        final Button closeWindow = new Button("Закрыть", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                windowAddTs.close();
            }
        });
        customLayout.addComponent(closeWindow, "closeWindow");

        formLayout.addComponent(customLayout);
        windowAddTs.setContent(formLayout);
        UI.getCurrent().addWindow(windowAddTs);
    }

    private class OneTsName implements Property.ValueChangeListener{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            hidden.pullDown("combo_device", (String) event.getProperty().getValue());
        }
    }

    private class OneFirstDate implements Property.ValueChangeListener{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            hidden.pullDown("first_date", sdf.format(event.getProperty().getValue()));
        }
    }

    private class OneLastDate implements Property.ValueChangeListener{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            hidden.pullDown("last_date", sdf.format(event.getProperty().getValue()));
        }
    }

    private class PrintOneReport implements Button.ClickListener{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void buttonClick(Button.ClickEvent event) {
            boolean selected = true;
            String name = hidden.pullUp("combo_device");
            try {
                if (name.equals(""))
                    selected = false;
            } catch (NullPointerException e){
                selected = false;
            }

            String from = hidden.pullUp("first_date");
            String to = hidden.pullUp("last_date");
            try {
                if (from.equals(""))
                    from = sdf.format(new Date());
            } catch (NullPointerException e){
                from = sdf.format(new Date());
            }

            try {
                if (to.equals(""))
                    to = sdf.format(new Date());
            } catch (NullPointerException e){
                to = sdf.format(new Date());
            }

            if (selected) {
                ArrayList<Report> report = dao.GetReport(name, from, to);
                if (report.size() != 0) {
                    String path = pdfCreator.CreateReport(report, name, from, to);
                    downloadReport.setEnabled(true);
                    Resource resource = new FileResource(new File(path));
                    FileDownloader fileDownloader = new FileDownloader(resource);
                    fileDownloader.extend(downloadReport);
                } else
                    Notification.show("Нет данных по перемещениям ТС: " + name);
            } else
                Notification.show("Выбирете ТС", Notification.Type.ERROR_MESSAGE);
        }
    }
}
