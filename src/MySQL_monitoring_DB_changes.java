import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;

public class MySQL_monitoring_DB_changes {

	public static void main(String[] args) throws IOException {
		System.out.println("Testing starts..");

		final Map<String, Long> tableMap = new HashMap<String, Long>();
		BinaryLogClient client =
	              new BinaryLogClient("localhost", 3306, "root", "password");

        client.registerEventListener(event -> {
            EventData data = event.getData();

            if(data instanceof TableMapEventData) {
                TableMapEventData tableData = (TableMapEventData)data;
                tableMap.put(tableData.getTable(), tableData.getTableId());
                System.out.println("tableData" + tableData.toString());
            }
        });
        client.connect();
    
		
		System.out.println("Testing ends..");
	}

}
