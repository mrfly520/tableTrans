package com.mrfly.java;


import com.mrfly.kt.bean.ColumnInfo;
import com.mrfly.kt.bean.ConnInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    private static final   String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String SQL = "SELECT * FROM ";// 数据库操作
    private ConnInfo connInfo;
     {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();//("can not load jdbc driver", e);
        }
    }
    public DatabaseUtil(ConnInfo connInfo){
         this.connInfo = connInfo;
    }

    public int execute(String sql) {
        Connection conn = getConnection();
        int count = 0;
        //String sql = "update students set Age='" + student.getAge() + "' where Name='" + student.getName() + "'";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            count = pstmt.executeUpdate();
            System.out.println("resutl: " + count);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int execute(String sql,List<?> list) {
        Connection conn = getConnection();
        int count = 0;
        //String sql = "update students set Age='" + student.getAge() + "' where Name='" + student.getName() + "'";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            int index =1;
            for (Object value : list) {
                pstmt.setObject(index,value);
                index++;
            }
            count = pstmt.executeUpdate();
            System.out.println("resutl: " + count);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    /**
     * 获取数据库连接
     *
     * @return
     */
    public  Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connInfo.getUrl(), connInfo.getUser(), connInfo.getPwd());
        } catch (SQLException e) {
            e.printStackTrace();//("get connection failure", e);
        }
        return conn;
    }


    /**
     * 关闭数据库连接
     * @param conn
     */
    public  void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();//("close connection failure", e);
            }
        }
    }

    /**
     * 获取数据库下的所有表名
     */
    public  List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[] { "TABLE" });
            while(rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();//("getTableNames failure", e);
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();//("close ResultSet failure", e);
            }
        }
        return tableNames;
    }

    /**
     * 获取表中所有字段名称
     * @param tableName 表名
     * @return
     */
    public  List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            //e.printStackTrace();//("getColumnNames failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();//("getColumnNames close pstem and connection failure", e);
                }
            }
        }
        return columnNames;
    }

    /**
     * 获取表中所有字段类型
     * @param tableName
     * @return
     */
    public  List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();//("getColumnTypes failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();//("getColumnTypes close pstem and connection failure", e);
                }
            }
        }
        return columnTypes;
    }
    public  List<ColumnInfo> getColumnInfoList(String tableName) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                ColumnInfo columnInfo = new ColumnInfo(rsmd.getColumnName(i + 1), rsmd.getColumnTypeName(i + 1), "",false);
                columnInfoList.add(columnInfo);
            }
            rs = pStemt.executeQuery("show full columns from " + tableName);
            int i = 0;
            while (rs.next()) {
                ColumnInfo columnInfo = columnInfoList.get(i);
                columnInfo.setComment(rs.getString("Comment"));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();//("getColumnTypes failure", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();//("getColumnComments close ResultSet and connection failure", e);
                }
            }
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();//("getColumnTypes close pstem and connection failure", e);
                }
            }
        }
        return columnInfoList;
    }
    /**
     * 获取表中字段的所有注释
     * @param tableName
     * @return
     */
    public  List<String> getColumnComments(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        List<String> columnComments = new ArrayList<>();//列名注释集合
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();//("getColumnComments close ResultSet and connection failure", e);
                }
            }
        }
        return columnComments;
    }
    //@Test
    public  void tt() {
        /*List<String> tableNames = getTableNames();
        System.out.println("tableNames:" + tableNames);
        for (String tableName : tableNames) {
            System.out.println("ColumnNames:" + getColumnNames(tableName));
            System.out.println("ColumnTypes:" + getColumnTypes(tableName));
            System.out.println("ColumnComments:" + getColumnComments(tableName));
        }*/
    }
}
