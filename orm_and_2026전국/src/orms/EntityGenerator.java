package orms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import utils.Connections;
import utils.Data;

public class EntityGenerator {

    static List<List<String>> data = new ArrayList<>();
    static Map<String, List<List<String>>> grouped;
    static String template = "";

    public static void initData() {
//        ^\s+(\S+)\t(\S+)\t(\S+)\t(\S*)\s*$
//        new ArrayList<>(Arrays.asList("$1", "$2", "$3", "$4")),
    	for(Data d : Connections.select("SELECT\n"
    			+ "    TABLE_NAME,\n"
    			+ "    COLUMN_NAME,\n"
    			+ "    COLUMN_TYPE,\n"
    			+ "    EXTRA\n"
    			+ "FROM information_schema.COLUMNS\n"
    			+ "WHERE TABLE_SCHEMA = DATABASE()\n"
    			+ "ORDER BY TABLE_NAME, ORDINAL_POSITION;")) {
    		data.add(Arrays.asList( 
    				d.stream()
    				.map(c -> c.toString())
    				.toArray(String[]::new)
    			));
    	}
    }

    static String convertJavaType(String mysqlType) {
    	if (mysqlType.contains("int")) return "int";
        if (mysqlType.contains("var")) return "String";
        if (mysqlType.contains("deci")) return "double";
        if (mysqlType.contains("double")) return "double";
        if (mysqlType.contains("datatime")) return "LocalDateTime";
        if (mysqlType.contains("date")) return "LocalDate";
        return mysqlType;
    }
    
    static String JavaType(String mysqlType) {
    	if (mysqlType.contains("int")) return "Integer";
        if (mysqlType.contains("var")) return "String";
        if (mysqlType.contains("deci") || mysqlType.contains("double")) return "Double";
        if (mysqlType.contains("datatime")) return "LocalDateTime";
        if (mysqlType.contains("date")) return "LocalDate";
        return mysqlType;
    }

    static void replaceTemplate() throws IOException {

        for (List<List<String>> info : grouped.values()) {
            String class_name = info.get(0).get(0);
            String id = info.get(0).get(1);
            String fields = info.stream()
                    .map(row -> String.format("public %s %s;", JavaType(row.get(2)), row.get(1)))
                    .collect(Collectors.joining("\n"));
            String rs_fields = info.stream()
                    .map(row -> String.format("e.%s= rs.get%s(\"%s\");", row.get(1), convertJavaType(row.get(2)), row.get(1)))
                    .collect(Collectors.joining("\n"));
            String const_fields = info.stream()
                    .map(row -> String.format("%s %s", convertJavaType(row.get(2)), row.get(1)))
                    .collect(Collectors.joining(","));
            String const_fields_init = info.stream()
                    .map(row -> String.format("this.%s = %s;", row.get(1), row.get(1)))
                    .collect(Collectors.joining("\n"));
            String column_names = info.stream()
                    .filter(row -> !row.get(3).contains("auto"))
                    .map(row -> row.get(1))
                    .collect(Collectors.joining(","));
            String values = IntStream.range(0, info.size() - 2).mapToObj(i -> "?").collect(Collectors.joining(","));
            String update = info.stream()
                    .map(row -> String.format("%s = ?", row.get(1)))
                    .collect(Collectors.joining(","));
            String sql_insert = info.stream()
                    .filter(row -> row.get(3).isBlank())
                    .map(row -> row.get(1))
                    .collect(Collectors.joining(","));
            String sql_update = sql_insert + ", " + id;

            String result = template.replace("${class_name}", class_name)
                    .replace("${fields}", fields)
                    .replace("${rs_fields}", rs_fields)
                    .replace("${id}", id)
                    .replace("${const_fields}", const_fields)
                    .replace("${const_fields_init}", const_fields_init)
                    .replace("${column_names}", column_names)
                    .replace("${values}", values)
                    .replace("${update}", update)
                    .replace("${sql_insert}", sql_insert)
                    .replace("${sql_update}", sql_update);

            Path outDir = Path.of("src/orms");
            Files.writeString(outDir.resolve(class_name + "Entity.java"), result);
        }
    }

    public static void main(String[] args) throws IOException {
        initData();
        template = Files.readString(Path.of("files/entity-template.txt"));
        grouped = data.stream().collect(Collectors.groupingBy(row -> row.get(0)));
        replaceTemplate();
    }
}
