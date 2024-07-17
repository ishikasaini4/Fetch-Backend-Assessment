package com.fetch.assessment.backend.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fetch.assessment.backend.ExceptionHandler.GlobalExceptionHandler;
import com.fetch.assessment.backend.Services.ReceiptService;
import com.fetch.assessment.backend.models.Receipt;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping(value = "/process", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, String>> createReceipt( @Valid @RequestBody String receipt) throws JsonProcessingException {

        Receipt saved = receiptService.saveReceipt(receipt);

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("id", saved.getId().toString());

        ResponseEntity<Map<String, String>> response = new ResponseEntity<>(responseMap, HttpStatus.CREATED);
        return response;
    }


    @GetMapping(value = "/{id}/points", produces = "application/json")
    public ResponseEntity<Map<String, String>> getPoints(@Valid @PathVariable String id){
        Optional<Receipt> receipt = receiptService.getReceipt(UUID.fromString(id));
        double points = 0;

        if(receipt.isPresent()) {
            points = receiptService.calculateReceiptPoints(receipt.get());
        }

        Map<String, String>responseMap = new HashMap<>();
        responseMap.put("points", String.valueOf(points));

        ResponseEntity<Map<String, String>> response = new ResponseEntity<>(responseMap, HttpStatus.OK);
        return response;
    }
}
