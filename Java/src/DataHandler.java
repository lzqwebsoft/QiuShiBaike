import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;


public class DataHandler {
    private String url;
    private String username;
    private String password;
    private Connection conn;

    public DataHandler() throws IOException, ClassNotFoundException, SQLException {
        // 加载数据库配置文件db_config.properties
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        prop.load(inputStream);
        if(inputStream == null) {
            throw new FileNotFoundException("Property file db_config.properties not found in the classpath");
        }
        username = prop.getProperty("username");
        password = prop.getProperty("password");
        String hostname = prop.getProperty("hostname");
        String port = prop.getProperty("port");
        String dbname = prop.getProperty("database");
        String drivername = prop.getProperty("drivername");
        // 加载驱动
        Class.forName(drivername);
        // 获得连接实例对象
        url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname;
        conn = DriverManager.getConnection(url, username, password);
    }

    public QiuShi getDataByIdx(int currentId) throws SQLException {
        String script = "select * from qiushi where id >= ? order by id ASC limit 0,1;";
        PreparedStatement ps=this.getConnection().prepareStatement(script);
        ps.setInt(1, currentId);
        ResultSet result = ps.executeQuery();
        QiuShi qiushi = null;
        if(result.next()){
            InputStream header = result.getBinaryStream("header");
            InputStream thumb = result.getBinaryStream("thumb");
            qiushi = new QiuShi(result.getInt("id"), result.getString("author"), header, result.getString("content"),
                thumb, result.getString("created_at"));
        }
        result.close();
        ps.close();
        return qiushi;
    }

    public QiuShi getPrevious(int currentId) throws SQLException {
        String script = "select * from qiushi where id < ? order by id DESC limit 0,1;";
        PreparedStatement ps=this.getConnection().prepareStatement(script);
        ps.setInt(1, currentId);
        ResultSet result = ps.executeQuery();
        QiuShi qiushi = null;
        if(result.next()){
            InputStream header = result.getBinaryStream("header");
            InputStream thumb = result.getBinaryStream("thumb");
            qiushi = new QiuShi(result.getInt("id"), result.getString("author"), header, result.getString("content"),
                thumb, result.getString("created_at"));
        }
        result.close();
        ps.close();
        return qiushi;
    }

    public QiuShi getNext(int currentId) throws SQLException {
        String script = "select * from qiushi where id > ? order by id ASC limit 0,1;";
        PreparedStatement ps=this.getConnection().prepareStatement(script);
        ps.setInt(1, currentId);
        ResultSet result = ps.executeQuery();
        QiuShi qiushi = null;
        if(result.next()){
            InputStream header = result.getBinaryStream("header");
            InputStream thumb = result.getBinaryStream("thumb");
            qiushi = new QiuShi(result.getInt("id"), result.getString("author"), header, result.getString("content"),
                thumb, result.getString("created_at"));
        }
        result.close();
        ps.close();
        return qiushi;
    }

    // 获取数据库连接对象，如果连接对象已关闭，则重新创建
    public Connection getConnection() throws SQLException {
        if(conn!=null && conn.isValid(60)) {
            return conn;
        } else {
            conn = DriverManager.getConnection(url, username, password);
            return conn;
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}