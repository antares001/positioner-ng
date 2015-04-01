package pmr.mvd.positioner.utils;

import pmr.mvd.positioner.bean.Report;

import java.util.ArrayList;
import java.util.UUID;

public class ExcelCreator {
    private String path;

    public ExcelCreator(){
        this.path = UUID.randomUUID().toString();
    }

    public String CreateReport(ArrayList<Report> arg, String name, String from, String to){
        return this.path;
    }
}
