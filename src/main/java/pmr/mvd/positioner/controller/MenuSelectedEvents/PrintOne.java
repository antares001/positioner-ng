package pmr.mvd.positioner.controller.MenuSelectedEvents;

import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.Report;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;
import pmr.mvd.positioner.utils.PdfCreator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PrintOne implements MenuBar.Command {
    private SqlDao dao = new SqlDao();
    private PdfCreator pdfCreator = new PdfCreator();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Window windowAddTs = new Window("Отчет для одного транспортного средства");

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

        final Button printReport = new Button("Распечатать");
        PrintOneReport printOneReport = new PrintOneReport();
        printReport.addClickListener(printOneReport);
        customLayout.addComponent(printReport, "printReport");

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
            String name = hidden.pullUp("combo_device");
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
            ArrayList<Report> report = dao.GetReport(name, from, to);
            pdfCreator.CreateReport(report, name, from, to);
        }
    }
}
