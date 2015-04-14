package cglotr.dbconnect;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.sql.*;

/**
 * Created by ClayFaith on 2015-02-05.
 */
public class Main extends Activity {

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        Log.d("connect", "ok i am alive");
        super.onCreate(saved_instance_state);
        setContentView(R.layout.main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("connect", "ok i am a thread");

                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    Driver driver = DriverManager.getDriver("jdbc:mysql//db4free.net:3306/fyyymysql");
                    Log.d("connect", "connected yay");
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.d("connect", "not connected");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
