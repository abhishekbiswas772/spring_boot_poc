package com.example.web_graph_builder.Graph.builder.GraphBuildingModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Controller
public class ChartController {

    @Autowired
    private GraphMakerManagerWrapper graphMakerManagerWrapper;
    List<GraphChartModel> graphChartModelsArray = Arrays.asList(
            new GraphChartModel("bankCapitalAdequacyRatio", "tick", "Banks' Capital Adequacy Ratios", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv"),
            new GraphChartModel("bankCashReserves", "tick", "Banks' Cash Reserves (monetary units)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv"),
            new GraphChartModel("bankCommercialLoans", "tick", "Banks' Loans to Non-Banks (monetary units)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv"),
            new GraphChartModel("bankInterbankLoans", "tick", "Banks' Loans to other Banks on the Interbank Market (monetary units)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv"),
            new GraphChartModel("bankDepositAccountHoldings", "tick", "Banks' Total Deposit Account Holdings (firms and households if included in the model) (monetary units)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv"),
            new GraphChartModel("bankDepositsFromHouseholds", "tick", "Banks' Deposits from the Household Sector (monetary units)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv"),

            new GraphChartModel("bankEquity", "tick", "Banks' Equity (monetary units)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv"),
//            new ChartModel("BankLeverage", "tick", "Banks' Leverage (total assets / equity)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\csv_processor\\output.csv")
            new GraphChartModel("bankRiskWeightedAssets", "tick", "Banks' Risk-Weighted Assets (monetary units)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv"),
            new GraphChartModel("bankStockPrice", "tick", "Banks' Stock Prices (monetary units)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv"),
            new GraphChartModel("bankTotalAssets", "tick", "Banks' Total Assets on their Balance Sheets (monetary units)", "E:\\java_proj\\Graph-builder\\src\\main\\java\\com\\example\\web_graph_builder\\Graph\\builder\\GraphBuildingModule\\csv_processor\\output.csv")
    );

    @GetMapping("/showChart")
    public String showChart(Model model) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            List<Future<GraphChartModel>> futures = executor.invokeAll(createTasks());

            for (Future<GraphChartModel> future : futures) {
                try {
                    GraphChartModel graphChartModel = future.get();
                    updateGraphChartModel(graphChartModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            model.addAttribute("chartModels", graphChartModelsArray);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return "chart";
    }

    private List<Callable<GraphChartModel>> createTasks() {
        return graphChartModelsArray.stream()
                .map(graphChartModel -> (Callable<GraphChartModel>) () -> {
                    String base64Image = graphMakerManagerWrapper.generateGraphFromCSV(
                            graphChartModel.getOutputCSVPath(),
                            graphChartModel.getxAxisParameter(),
                            graphChartModel.getyAxisParameter(),
                            graphChartModel.getNameOfGraph()
                    );
                    graphChartModel.setBase64GraphImg(base64Image);
                    return graphChartModel;
                }).toList();
    }

    private void updateGraphChartModel(GraphChartModel graphChartModel) {
        for (int i = 0; i < graphChartModelsArray.size(); i++) {
            if (graphChartModelsArray.get(i).getNameOfGraph().equals(graphChartModel.getNameOfGraph())) {
                graphChartModelsArray.set(i, graphChartModel);
                break;
            }
        }
    }
}