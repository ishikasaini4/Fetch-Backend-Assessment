package com.fetch.assessment.backend.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetch.assessment.backend.ExceptionHandler.GlobalExceptionHandler;
import com.fetch.assessment.backend.Repositories.ReceiptRepository;
import com.fetch.assessment.backend.Utils.ItemUtils;
import com.fetch.assessment.backend.models.Item;
import com.fetch.assessment.backend.models.Receipt;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Transactional
    public Receipt saveReceipt (String receipt) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        Receipt saved = new Receipt();

        // Parse JSON string to JsonNode
        JsonNode jsonNode = objectMapper.readTree(receipt);


        saved.setRetailer(jsonNode.get("retailer").asText());
        saved.setPurchaseDate(jsonNode.get("purchaseDate").asText());
        saved.setPurchaseTime(jsonNode.get("purchaseTime").asText());
        saved.setTotal(jsonNode.get("total").asText());

        // Extract items array from JsonNode
        JsonNode itemsNode = jsonNode.get("items");
        List<Item> items = objectMapper.convertValue(itemsNode, new TypeReference<List<Item>>() {});

        if(ItemUtils.isItemListNullOrEmpty(items)){
            GlobalExceptionHandler.handleCustomException("items", "items cannot be null or empty");
        }

        Map<String, String> itemErrors = new LinkedHashMap<>();

        for (Item item : items){
            if(ItemUtils.isValidItemDescription(item.getShortDescription())){
                String errorMsg = "Item Description";
                if(!itemErrors.containsKey(errorMsg)){
                    itemErrors.put(errorMsg, "Item description cannot be null or empty");
                }
            }

            if(ItemUtils.isValidItemPrice(item.getPrice())){
                String errorMsg = "Item price";
                if(!itemErrors.containsKey(errorMsg)){
                    itemErrors.put(errorMsg, "Item price is not valid");
                }
            }
        }

        if(itemErrors.size()>0){
            GlobalExceptionHandler.handleCustomException(itemErrors);
        }

        String itemsString = items.stream().map(Item::toString)// Convert each Item to its string representation
                .reduce((item1, item2) -> item1 + "; " + item2) // Concatenate with commas
                .orElse("");

        saved.setItems(itemsString);
        return receiptRepository.save(saved);
    }

    public Optional<Receipt> getReceipt(UUID id){
        return receiptRepository.findById(id);
    }

    public double calculateReceiptPoints(Receipt receipt){
        double points = 0;

        if(receipt==null){
            return points;
        }

        //1 point for each alphabet/number in retailer name
        points+=receipt.getRetailer().chars()
                .filter(c-> Character.isAlphabetic(c) || Character.isDigit(c)).count();

        double receiptTotal = Double.parseDouble(receipt.getTotal());

        //50 points for 0 cents in receipt total
        if(receiptTotal % 1==0) {
            points += 50;
        }

        //25 points if receipt total is a multiple of 0.25
        if(receiptTotal % 0.25==0){
            points+=25;
        }


        String[] items = receipt.getItems().split(";");

        //5 points for every two items
        points+= (items.length/2) * 5;

        //price * 0.2 if description length is divisible by 3
        for (int i = 0; i<items.length; i++){

            String description = items[i].split(",")[0].split("=")[1].trim();
            StringBuilder priceSb = new StringBuilder(items[i].split(",")[1].split("=")[1]);

            double price = Double.parseDouble(priceSb.toString().substring(0, priceSb.indexOf(")")));

            if(description.length()%3==0){
                points += Math.ceil(price * 0.2);
            }
        }

        //6 points if purchase day is odd
        String purchaseDate = receipt.getPurchaseDate();
        int day = Integer.parseInt(purchaseDate.split("-")[2]);
        if (day % 2 != 0) {
            points += 6;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime purchaseTime = LocalTime.parse(receipt.getPurchaseTime(), timeFormatter);
        LocalTime start = LocalTime.of(14, 0);              // 2:00pm
        LocalTime end = LocalTime.of(16, 0);                // 4:00pm

        //10 points if time is after 2pm and before 4pm
        if (purchaseTime.isAfter(start) && purchaseTime.isBefore(end)) {
            points += 10;
        }

        return points;

    }
}
