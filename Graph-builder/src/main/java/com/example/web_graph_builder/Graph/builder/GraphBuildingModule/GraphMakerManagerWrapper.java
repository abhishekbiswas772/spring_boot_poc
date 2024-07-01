package com.example.web_graph_builder.Graph.builder.GraphBuildingModule;
import com.example.web_graph_builder.Graph.builder.GraphBuildingModule.image_processor.ImageProcessor;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class GraphMakerManagerWrapper {

    @Autowired
    private ImageProcessor imageProcessor;


    public String generateGraphFromCSV(String csvFilePath, String xColumn, String yColumnKeyword, String chartName) throws IOException {
        List<String> lines = Files.readAllLines(new File(csvFilePath).toPath());
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("The CSV file is empty.");
        }
        String[] headers = lines.get(0).replace("\"", "").split(",");

        int xColumnIndex = -1;
        List<Integer> yColumnIndexes = new ArrayList<>();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(xColumn)) {
                xColumnIndex = i;
            } else if (headers[i].contains(yColumnKeyword)) {
                yColumnIndexes.add(i);
            }
        }

        if (xColumnIndex == -1 || yColumnIndexes.isEmpty()) {
            throw new IllegalArgumentException("Invalid column names provided.");
        }

        List<List<Double>> data = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).replace("\"", "");
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] values = line.split(",");
            if (values.length <= xColumnIndex) {
                System.err.println("Skipping line due to insufficient columns: " + line);
                continue;
            }
            List<Double> row = new ArrayList<>();
            try {
                row.add(Double.parseDouble(values[xColumnIndex]));
                for (int index : yColumnIndexes) {
                    if (index < values.length) {
                        row.add(Double.parseDouble(values[index]));
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Skipping line due to number format issue: " + line);
                continue;
            }
            data.add(row);
        }

        List<Double> xData = new ArrayList<>();
        for (List<Double> row : data) {
            xData.add(row.get(0));
        }

        List<List<Double>> yData = new ArrayList<>();
        for (int i = 0; i < yColumnIndexes.size(); i++) {
            List<Double> yColumnData = new ArrayList<>();
            for (List<Double> row : data) {
                if (row.size() > i + 1) {
                    yColumnData.add(row.get(i + 1));
                }
            }
            yData.add(yColumnData);
        }

        XYChart chart = new XYChartBuilder()
                            .width(800)
                            .height(600)
                            .title(yColumnKeyword + " Over Time")
                            .xAxisTitle(xColumn)
                            .yAxisTitle("")
                            .build();

        for (int i = 0; i < yColumnIndexes.size(); i++) {
            if (xData.size() == yData.get(i).size()) {
                chart.addSeries(headers[yColumnIndexes.get(i)], xData, yData.get(i))
                        .setMarker(SeriesMarkers.NONE)
                        .setLineColor(XChartSeriesColors.PURPLE);
            } else {
                System.err.println("Data size mismatch for series: " + headers[yColumnIndexes.get(i)]);
            }
        }

        XYStyler styler = chart.getStyler();
        styler.setChartBackgroundColor(Color.white);
        styler.setPlotBackgroundColor(Color.white);
        styler.setPlotGridLinesVisible(false);
        styler.setChartTitleVisible(true);
        styler.setLegendVisible(false);
        styler.setMarkerSize(5);
        styler.setAxisTitlesVisible(true);
        styler.setAxisTicksVisible(true);
        styler.setXAxisLabelRotation(45);
        styler.setXAxisMin(0.0);
        styler.setYAxisMin(0.0);
        styler.setYAxisMax((double) yData.get(0).stream().max(Double::compare).orElse(1.0));
        styler.setXAxisTickMarkSpacingHint(50);
        styler.setYAxisTickMarkSpacingHint(20);
        styler.setPlotBorderVisible(false);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapEncoder.saveBitmap(chart, baos, BitmapFormat.PNG);
        byte[] bytes = baos.toByteArray();
        String base64GenImageString = Base64.getEncoder().encodeToString(bytes);
        try {
            return this.imageProcessor.processImage(base64GenImageString);
        }catch (Exception exception){
            System.out.print(exception.getLocalizedMessage());
            throw new IllegalArgumentException("Invalid column names provided.");
        }
    }
}
