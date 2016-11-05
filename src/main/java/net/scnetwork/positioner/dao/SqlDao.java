package net.scnetwork.positioner.dao;

import net.scnetwork.positioner.domain.BeanSettings;
import net.scnetwork.positioner.domain.Positions;
import net.scnetwork.positioner.domain.Report;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SqlDao {
    @Autowired
    private SqlSession sqlSession;

    public BeanSettings getCredentials(String username){
        return sqlSession.selectOne("selectCredentials", username);
    }

    public void insertData(Positions positions){
        sqlSession.insert("insertPositions", positions);
    }

    public List<Report> getReport(){
        return sqlSession.selectList("getReport");
    }
}
