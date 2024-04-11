package com.example.BriefMe.controllers;

import com.example.BriefMe.service.client.YoutubeVideoSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class YoutubeVideoSummaryController {

    @Autowired
    YoutubeVideoSummaryService youtubeVideoSummaryService;

    @GetMapping("/get-summary")
    public ResponseEntity<String> generateSummary(@RequestParam String video, @RequestParam int lines){
        String summary = youtubeVideoSummaryService.generateSummary(video, lines);
        return ResponseEntity.ok(summary);
    }

}
