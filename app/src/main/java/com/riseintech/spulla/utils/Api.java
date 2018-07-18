package com.riseintech.spulla.utils;

/**
 * Created by user on 10/12/2016.
 */

public class Api {
    public static String BASE_URL = "http://spulla.com/admin/spulla_api/";
        public static String Spulla_Home_URL = BASE_URL + "jsonhome.php";

    public static String GET_FREQUEST = BASE_URL + "get_frequest.php";

    public static String FACT_DETAILS = BASE_URL + "factsdetail.php";
    public static String GOSSIP_DETAILS = BASE_URL + "gosipdetail.php";
    public static String Select_Cat = BASE_URL + "select_cat.php";

    ///////////////coach details api
    public static String Coach_Search = BASE_URL + "searchcoachvideo.php";
    public static String Coach_AgeGrp = BASE_URL + "findcoach_agegroup.php";
    ////////////
    public static String Tournament_Search = BASE_URL + "findtournament.php";
    public static String Player_Search = BASE_URL + "searchplayer.php";

    public static String Add_Player = BASE_URL + "addplayer.php";
    public static String Get_Form_Category = BASE_URL + "get_form_category.php";

    public static String Player_Details = BASE_URL + "playerdetail.php";
    public static String Send_Otp = BASE_URL + "otp_signup.php";
    public static String Login = BASE_URL + "login_otp.php";


    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    public static String Get_Player_Details = BASE_URL + "grtplayer_detail.php";
    public static String Add_Comments = BASE_URL + "addcomments.php";
    public static String Get_Comments = BASE_URL + "commentsdetail.php";

    //////////////////////////////////// tournamnet details Api
    public static String Tournament_Details = BASE_URL + "tournamentdetail.php";
    public static String Add_Tournament = BASE_URL + "add_tournament.php";
    public static String Tourn_Age_Group = BASE_URL + "tournament_agegroup.php";

    ////////////////////////////
    public static String Customer_Support = BASE_URL + "customer_support.php";

    public static String Sopping_Details = BASE_URL + "e_product_detail.php";

    public static String Sopping_SubCat = BASE_URL + "subcategory_detail.php";

    public static String Sopping_SRating = BASE_URL + "review_ratting.php";
    public static String Sopping_SgetRating = BASE_URL + "get_rating.php";

    public static String Sopping_Addcard = BASE_URL + "addto_card.php";

    public static String URL_STORE_TOKEN = BASE_URL + "gcm_register.php";

    public static String URL_SEND_REQUEST = BASE_URL + "fridens_request_status.php";

    public static String URL_ADD_REQUEST = BASE_URL + "add_frends.php";

    public static String URL_FRIEND_LIST = BASE_URL + "friend_list.php";
    public static String Irrelevant_Profile_LIST = BASE_URL + "rejectlist.php";

    public static String URL_FETCH_MESSAGES = BASE_URL + "message_detail.php";
    public static String URL_SEND_MESSAGE = BASE_URL + "messege.php";


    //like gossip or facts api
    public static String Add_Like_Url = BASE_URL + "addlike.php";
    public static String Checkuser_like_Url = BASE_URL + "checkuserlike.php";

    ///deliv address url
    public static String Add_Deliv_AddressUrl = BASE_URL + "send_address.php";
    public static String Get_Deliv_AddressUrl = BASE_URL + "get_addresss.php";

    ////////////////Addto cart apis
    public static String Remove_Pdt_Url = BASE_URL + "remove_cart.php";
    public static String CartList_Url = BASE_URL + "addtocart_list.php";
    public static String CartList_Size_Url = BASE_URL + "cart_item.php";

    public static String Search_Hot_Deal_Url = BASE_URL + "hot_deal.php";
    public static String Hot_Deal_Url = BASE_URL + "hot_deal_all.php";
    ;

}
