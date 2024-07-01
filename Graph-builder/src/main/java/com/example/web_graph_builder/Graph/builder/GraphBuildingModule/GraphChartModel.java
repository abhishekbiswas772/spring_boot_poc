package com.example.web_graph_builder.Graph.builder.GraphBuildingModule;

public class GraphChartModel {
    private String yAxisParameter;
    private String xAxisParameter;
    private String nameOfGraph;
    private String outputCSVPath;

    private String base64GraphImg;
    public GraphChartModel(String yAxisParameter, String xAxisParameter, String nameOfGraph, String outputCSVPath){
        this.yAxisParameter = yAxisParameter;
        this.xAxisParameter = xAxisParameter;
        this.nameOfGraph = nameOfGraph;
        this.outputCSVPath = outputCSVPath;
    }

    public void setBase64GraphImg(String base64GraphImg){this.base64GraphImg = base64GraphImg;}
    public String getBase64GraphImg(){return this.base64GraphImg;}
    public String getOutputCSVPath(){return this.outputCSVPath;}
    public String getyAxisParameter(){return this.yAxisParameter;}
    public String getxAxisParameter(){return this.xAxisParameter;}
    public String getNameOfGraph(){return this.nameOfGraph;}

}
