package com.example.web_graph_builder.Graph.builder.FileLoadAndSaveModule;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class FileController {

    @GetMapping("/file")
    public String fileUploadPage() {
        return "file";  // This should match the name of your HTML file in the templates directory (file.html)
    }

    @PostMapping("/notify-folder-creation")
    public ResponseEntity<String> notifyFolderCreation(@RequestBody Map<String, String> request) {
        String folderName = request.get("folderName");
        System.out.println("Folder created on client: " + folderName);
        return ResponseEntity.ok("Server notified of folder creation.");
    }
}
