import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventMetadata;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;

public class MySQL_monitoring_DB_changes {

	static String driver;
	static String url;
	static String userName;
	static String password;
	static String hostname;
	static Connection conn;
	static ResultSet rs;
	static Statement testStatement;

	public static void main(String[] args) throws IOException {
		System.out.println("Testing starts..");

		String db_name = "lpp43997c";
		// *** MySQL section start ***
		driver = "com.mysql.jdbc.Driver";
		hostname = "localhost";
		url = "jdbc:mysql://localhost:3306/lpp43997c?useUnicode=true&characterEncoding=UTF-8&useFastDataParsing=false";
		userName = "root";
		password = "password";
		// *** MySQL section end ***

		final Map<String, Long> tableMap = new HashMap<String, Long>();
		BinaryLogClient client = new BinaryLogClient(hostname, 3306, userName, password);

		client.registerEventListener(event -> {
			EventData data = event.getData();

			if (data instanceof TableMapEventData) {
				TableMapEventData tableData = (TableMapEventData) data;
				String tableData_dataBase = tableData.getDatabase();
				tableMap.put(tableData.getTable(), tableData.getTableId());
				System.out.println("General information about a table was requested: " + tableData.toString());
			} else if (data instanceof UpdateRowsEventData) {
				UpdateRowsEventData eventData = (UpdateRowsEventData) data;
				TableMapEventMetadata my = new TableMapEventMetadata();

				long updatedTableName = eventData.getTableId();

				for (Entry<Serializable[], Serializable[]> row : eventData.getRows()) {
					{
						System.out.println("The following db table row was updated: " + getProductMap(db_name, row.getValue()));
					}
				}
			}

		});
		client.connect();
		System.out.println("Testing ends..");
	}

	static Map<String, String> getProductMap(String db_name, Object[] product) {
		Map<String, String> map = new HashMap<>();
		int tableColumnCount = product.length;

		for (int i = 0; i < tableColumnCount; i++) {
			int columnCount = i + 1;
			map.put("column" + columnCount, java.lang.String.valueOf(product[i]));
		}

		return map;
	}

}
