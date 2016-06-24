package net.scnetwork.positioner.dao;

import net.scnetwork.positioner.domain.BeanSettings;
import net.scnetwork.positioner.domain.Positions;
import net.scnetwork.positioner.domain.Report;

import java.util.Date;

public interface ISqlDao {

    BeanSettings getCredentials(String username);

    boolean insertPath(Positions positions);

    Report getReport(String id, Date start, Date end);
}
