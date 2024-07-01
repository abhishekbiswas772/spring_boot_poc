package com.example.web_graph_builder.Graph.builder;
import com.example.web_graph_builder.Graph.builder.GraphBuildingModule.csv_processor.MasterModelCSVProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class GraphBuilderApplication {

	public static void main(String[] args){
		SpringApplication.run(GraphBuilderApplication.class, args);
//		MasterModelCSVProcessor graphPackgae = new MasterModelCSVProcessor();
//		graphPackgae.processMasterModelCSV("E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\csv_processor\\MasterModel.csv", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\csv_processor\\output.csv");
	}

}
