package app.com.runner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import app.com.config.AppConstant;

@Component
public class SQLRunner implements ApplicationRunner {
	
    @Autowired
    DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    	Connection conn = dataSource.getConnection();
        Statement stmt = null;
        ResultSet rs = null; //ResultSet 객체 선언

        try {
        	stmt = conn.createStatement();
            String sql = "select code, message from cod_message";
            rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                AppConstant.codeMap.put(rs.getString(1), rs.getString(2));
            }

            rs.close(); stmt.close(); conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if(conn!=null) try {conn.close();} catch (SQLException e) {}
        }
    }
}
