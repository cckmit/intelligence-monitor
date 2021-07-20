package com.zhikuntech.intellimonitor.structuremonitor.domain.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author 滕楠
 * @className SqlServerUtil
 * @create 2021/7/14 11:06
 **/
public class SqlServerUtil {
    public static void main(String[] args) {
        {
            Connection con;
            Statement st;
            ResultSet result;

            String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String dbURL = "jdbc:sqlserver://192.168.30.60:1433;integratedSecurity=false; DatabaseName=振动监测数据库";
            String user = "sa";
            String pwd = "develop";
            //jdbc:sqlserver://localhost:1433;integratedSecurity=true选择windows本地验证登陆。
            String sql = "select top 1 * from \"国电普陀风电场_1号风机_塔筒振动\"";

            try {
                Class.forName(driverName);
                con = DriverManager.getConnection(dbURL, user, pwd);
                st = con.createStatement();
                result = st.executeQuery(sql);
                //获取表字段数目
                int col = result.getMetaData().getColumnCount();

                while (result.next()) {
                    for (int i = 2; i <= 13; i++) {
                        System.out.print("通道" + (i - 1) +"#"+ result.getString(i) + "\t");
                    }
                    System.out.println();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
