package com.riseintech.spulla.utils;

import com.riseintech.spulla.model.MainCardModel;

import java.util.ArrayList;



/**
 * Created by launk on 12/03/15.
 */
public class ObjectStoreUtil {

    private static ArrayList<MainCardModel> favoriteCardModels = new ArrayList<MainCardModel>();
    private static ArrayList<MainCardModel> cartCardModels = new ArrayList<MainCardModel>();
    private static ArrayList<MainCardModel> recentlyViewedModels = new ArrayList<MainCardModel>();

    public static ArrayList<MainCardModel> getFavoriteModel(){
        return favoriteCardModels;
    }

    public static int countFavorite() {
        return favoriteCardModels.size();
    }

    public static ArrayList<MainCardModel> getCartModel(){
        return cartCardModels;
    }

    public static int countCart() {
        return cartCardModels.size();
    }

    public static void clearAll(){
        favoriteCardModels.clear();
        favoriteCardModels.clear();
    }

    public static ArrayList<MainCardModel> getRecentlyViewedModels() {
        return recentlyViewedModels;
    }

    public static void addRecentlyViewed(MainCardModel cardModel){
        if(recentlyViewedModels.isEmpty()){
            recentlyViewedModels.add(cardModel);
        }else{
            /*remove duplicate object from Recently Viewed List*/
            boolean duplicate = false;
            for(int i=0; i<recentlyViewedModels.size(); i++){
                if(recentlyViewedModels.get(i).getId().equals(cardModel.getId())){
                    duplicate = true;
                    break;
                }
            }
            if(!duplicate)
                recentlyViewedModels.add(cardModel);
        }
    }

    public static void setRecentlyViewedModels(ArrayList<MainCardModel> recentlyViewedModels) {
        ObjectStoreUtil.recentlyViewedModels = recentlyViewedModels;
    }
}
