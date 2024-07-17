package com.fetch.assessment.backend.Utils;

import com.fetch.assessment.backend.models.Item;

import java.util.List;

public class ItemUtils {

    public static boolean isValidItemPrice(String price){
        return price.matches("^\\d+(\\.\\d{1,2})?$");
    }

    public static boolean isItemListNullOrEmpty(List<Item> items){
        return items==null || items.size()==0;
    }

    public static boolean isValidItemDescription(String description){
        return description==null || description.trim().length()==0;
    }
}
