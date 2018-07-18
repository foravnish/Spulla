package com.riseintech.spulla.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.riseintech.spulla.AsyncTaskService.AddCommentAsync;
import com.riseintech.spulla.FactDetailsActivity;
import com.riseintech.spulla.GossipActivity;
import com.riseintech.spulla.PlayerutubeActivity;
import com.riseintech.spulla.R;
import com.riseintech.spulla.adapter.GossipCommentAdapter;
import com.riseintech.spulla.adapter.VideoAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.Util;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by user on 11/10/2016.
 */

public class FactsFragments extends Fragment implements View.OnClickListener {
    Context context;
    private ArrayList<HashMap<String, String>> factslist;
    //////////comment
    TextView cn_name, cmt_time, cmtns_txv, cn_name1, cmt_time1, cmtns_txv1, cn_name2, cmt_time2, cmtns_txv2;
    ImageView cmt_img, cmt_img1, cmt_img2, whtsapp_btn;
    LinearLayout cmnt_p_ly, cmnt_p_ly1, cmnt_p_ly2, view_more_ly;
    View divider, divider1;
    String DEVELOPER_KEY = "AIzaSyAEYb0pkeDUxgnD-aGPBmKkwRO297Wpa3o";
    /////////////////////
    ImageView fb_btn, fact_bg, twt_btn;
    TextView gosip_hd, gsp_date1, gssp_dtls, no_cmmts, subtit, view_more, like;
    //////////////////////////////////////////
    int position;
    String url = "dkd";
    String types = "dkd";
    String cmt_img_url = "fdsf", cmt_img_url1 = "dsds", cmt_img_url2 = "dfsf";
    EditText cmts_hd;
    ArrayList<HashMap<String, String>> cmtlist = new ArrayList<>();
    String type;
    GossipCommentAdapter gossipCommentAdapter;
    ImageView play;
    YouTubeThumbnailView thumbnailview;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;


    int total_like;
    int userlike;

    public static FactsFragments newInstance(int position, ArrayList<HashMap<String, String>> factslist, String type) {
        Bundle args = new Bundle();
        args.putInt("pos", position);
        args.putSerializable("factslist", factslist);
        args.putString("type", type);
        FactsFragments fragment = new FactsFragments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        factslist = (ArrayList<HashMap<String, String>>) getArguments().getSerializable("factslist");
        position = getArguments().getInt("pos", 0);
        types = getArguments().getString("type");

        // Log.e("factlist", "" + factslist);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.factsdetailsadapter, container, false);
        url = factslist.get(position).get("img");
        type = factslist.get(position).get("types");

        Log.d("gdfghfdhfghfghf2",url);
        Log.d("gdfghfdhfghfghf",type);
        /* Enabling strict mode */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LinearLayout add_cmt = (LinearLayout) view.findViewById(R.id.add_cmt);
        add_cmt.setOnClickListener(this);

        //  Toast.makeText(context, "onCreateView", Toast.LENGTH_SHORT).show();
        view_more_ly = (LinearLayout) view.findViewById(R.id.view_more_ly);
        view_more_ly.setVisibility(View.VISIBLE);

        /////////////////////////
        divider = (View) view.findViewById(R.id.divider);
        divider1 = (View) view.findViewById(R.id.divider1);
        divider.setVisibility(View.GONE);
        divider1.setVisibility(View.GONE);

        cmnt_p_ly = (LinearLayout) view.findViewById(R.id.cmnt_p_ly);
        cmnt_p_ly.setVisibility(View.GONE);

        cmnt_p_ly1 = (LinearLayout) view.findViewById(R.id.cmnt_p_ly1);
        cmnt_p_ly1.setVisibility(View.GONE);

        cmnt_p_ly2 = (LinearLayout) view.findViewById(R.id.cmnt_p_ly2);
        cmnt_p_ly2.setVisibility(View.GONE);

        cmt_img = (ImageView) view.findViewById(R.id.cmt_img);
        cmt_img.setOnClickListener(this);
        cn_name = (TextView) view.findViewById(R.id.cn_name);
        cmt_time = (TextView) view.findViewById(R.id.cmt_time);
        cmtns_txv = (TextView) view.findViewById(R.id.cmtns_txv);

        cmt_img1 = (ImageView) view.findViewById(R.id.cmt_img1);
        cmt_img1.setOnClickListener(this);
        cn_name1 = (TextView) view.findViewById(R.id.cn_name1);
        cmt_time1 = (TextView) view.findViewById(R.id.cmt_time1);
        cmtns_txv1 = (TextView) view.findViewById(R.id.cmtns_txv1);

        cmt_img2 = (ImageView) view.findViewById(R.id.cmt_img2);
        cmt_img2.setOnClickListener(this);
        cn_name2 = (TextView) view.findViewById(R.id.cn_name2);
        cmt_time2 = (TextView) view.findViewById(R.id.cmt_time2);
        cmtns_txv2 = (TextView) view.findViewById(R.id.cmtns_txv2);

        ///////////////////////////////////////////////////
        like = (TextView) view.findViewById(R.id.like);
        view_more = (TextView) view.findViewById(R.id.view_more);
        view_more.setOnClickListener(this);
        no_cmmts = (TextView) view.findViewById(R.id.no_cmmts);


        cmts_hd = (EditText) view.findViewById(R.id.cmts_hd);
        cmts_hd.setOnClickListener(this);
        fact_bg = (ImageView) view.findViewById(R.id.fact_bg);
        fb_btn = (ImageView) view.findViewById(R.id.fb_btn);
        twt_btn = (ImageView) view.findViewById(R.id.twt_btn);
        whtsapp_btn = (ImageView) view.findViewById(R.id.whtsapp_btn);

        play = (ImageView) view.findViewById(R.id.play);
        thumbnailview = (YouTubeThumbnailView) view.findViewById(R.id.thumbnailview);

        fb_btn.setOnClickListener(this);
        whtsapp_btn.setOnClickListener(this);
        twt_btn.setOnClickListener(this);
        fact_bg.setOnClickListener(this);
        gosip_hd = (TextView) view.findViewById(R.id.gosip_hd);
        gsp_date1 = (TextView) view.findViewById(R.id.gsp_date1);
        gssp_dtls = (TextView) view.findViewById(R.id.gssp_dtls);
        subtit = (TextView) view.findViewById(R.id.subtit);
        like.setOnClickListener(this);
        // ArrayList<HashMap<String, String>> hashMaps = total_cmmtLists.get(FactDetailsActivity.currentpos);

//        Util.showImage(context, url, fact_bg);

        if (type.equals("image")) {
            Util.showImage(context, url, fact_bg);
            fact_bg.setVisibility(View.VISIBLE);
            thumbnailview.setVisibility(View.GONE);
            play.setVisibility(View.GONE);
          //  Toast.makeText(context, ""+url, Toast.LENGTH_SHORT).show();

        }

        else if (type.equals("video")) {
            fact_bg.setVisibility(View.GONE);
            thumbnailview.setVisibility(View.VISIBLE);
            play.setVisibility(View.VISIBLE);

            thumbnailview.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                    YouTubeThumbnailLoader thumbnailLoader) {
                    youTubeThumbnailLoader = thumbnailLoader;
                    thumbnailLoader.setOnThumbnailLoadedListener(new VideoAdapter.ThumbnailListener());
                    youTubeThumbnailLoader.setVideo(url);

                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    String errorMessage =
                            String.format("onInitializationFailure (%1$s)",
                                    youTubeInitializationResult.toString());
                    //  Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                }
            });


           // Util.showImage(context, "http://spulla.com/admin/images/test_588de12f0e017.jpg", fact_bg);
         //   Toast.makeText(context, "video", Toast.LENGTH_SHORT).show();
          //  Toast.makeText(context, ""+url, Toast.LENGTH_SHORT).show();
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String VideoURL = url;
               // Log.d("gdfdhbfdgdhbfh",videoView.toString());
                view.getContext().startActivity(new Intent(view.getContext(), PlayerutubeActivity.class).putExtra("video_url", VideoURL));

              /*  Log.e("video", ": " + videoItems.get(position).get("video").toString());
                String VideoURL = videoItems.get(position).get("video").toString();
                Intent videoIntent = new Intent(view.getContext(), VideoViewActivity.class);
                videoIntent.putExtra("video_url", VideoURL);
                view.getContext().startActivity(videoIntent);*/
            }
        });


        // new LoadImageAsync(FactDetailsActivity.this, url, fact_bg).execute();
        gosip_hd.setText(factslist.get(position).get("topic"));
        gsp_date1.setText(factslist.get(position).get("date_time"));
        gssp_dtls.setText(Html.fromHtml(factslist.get(position).get("description")));
        subtit.setText(Html.fromHtml(factslist.get(position).get("subtitle")));
        if (InternetStatus.isConnectingToInternet(context)) {
            if (type.equalsIgnoreCase("facts")) {
                new GetCommentDetails(context, "facts", factslist.get(position).get("id"), cmtlist).execute();
            } else {
                new GetCommentDetails(context, "gosip", factslist.get(position).get("id"), cmtlist).execute();

            }
        }
        gossipCommentAdapter = new GossipCommentAdapter(context, cmtlist, true);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cmt_img:
                Util.showFullImageDialog(context, cmt_img_url, cn_name.getText().toString());
                break;
            case R.id.cmt_img1:
                Util.showFullImageDialog(context, cmt_img_url1, cn_name1.getText().toString());
                break;
            case R.id.cmt_img2:
                Util.showFullImageDialog(context, cmt_img_url2, cn_name2.getText().toString());
                break;
            case R.id.cmts_hd:
                //showAllComments(context);
                if (cmtlist.size() > 0) {
                    addCommentDialog(context);
                }
                break;
            case R.id.fact_bg:
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.getWindow()
                        .getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.showfullimage);
                ImageView back_img = (ImageView) dialog.findViewById(R.id.back_img);
                ImageView fact_image = (ImageView) dialog.findViewById(R.id.fact_image);
                TextView title = (TextView) dialog.findViewById(R.id.title);
                //Util.showImage(context, factslist.get(FactDetailsActivity.currentpos).get("img"), fact_image);
                if (type.equalsIgnoreCase("facts")) {
                    Util.showImage(context, factslist.get(FactDetailsActivity.currentpos).get("img"), fact_image);
                    // title.setText(factslist.get(FactDetailsActivity.currentpos).get("topic"));
                    title.setText("Facts");
                } else {
                    Util.showImage(context, factslist.get(GossipActivity.currentpos).get("img"), fact_image);
                    //title.setText(factslist.get(GossipActivity.currentpos).get("topic"));
                    title.setText("Gossips");

                }
                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(fact_image);
                //photoViewAttacher.onDrag(2,2);
                photoViewAttacher.update();
                back_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetStatus.isConnectingToInternet(context)) {
                            if (type.equalsIgnoreCase("facts")) {
                                new GetCommentDetails(context, "facts", factslist.get(position).get("id"), cmtlist).execute();
                            } else {
                                new GetCommentDetails(context, "gosip", factslist.get(position).get("id"), cmtlist).execute();

                            }
                        }
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case R.id.com_send:

                break;
            case R.id.like:
                if (like.getText().toString().isEmpty() == false) {
                    total_like = Integer.valueOf(like.getText().toString().replaceAll(" Like", ""));
                } else {
                    total_like = 0;
                }
                if (userlike == 0) {
                    userlike = 1;
                } else {
                    userlike = 0;

                }
                //Log.e("total like", ": " + total_like + ", user_like " + userlike);
                if (InternetStatus.isConnectingToInternet(context)) {
                    new AddLikeAsync(context, type, factslist.get(position).get("id"), userlike).execute();
                }
                break;
            case R.id.whtsapp_btn:
                Uri myImageUri1 = Uri.fromFile(String_to_File(url));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                //Target whatsapp:
                shareIntent.setPackage("com.whatsapp");
                //Add text and then Image URI
                shareIntent.putExtra(Intent.EXTRA_TEXT, gosip_hd.getText().toString()+"\n\n"+gsp_date1.getText().toString()+"\n\n"+gssp_dtls.getText().toString());
                shareIntent.putExtra(Intent.EXTRA_REFERRER_NAME, "https://play.google.com/store/apps/details?id=com.risein.suplla");
                shareIntent.putExtra(Intent.EXTRA_STREAM, myImageUri1);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //     ToastHelper.MakeShortText("Whatsapp have not been installed.");
                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fb_btn:
                shareOnFb(gosip_hd.getText().toString(), gssp_dtls.getText().toString(),
                        url, url);
                break;
            case R.id.twt_btn:
                URL linkurl = null;
                try {
                    linkurl = new URL("https://play.google.com/store/apps/details?id=com.risein.suplla");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Uri myImageUri = Uri.fromFile(String_to_File(url));
                TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                        .text(gosip_hd.getText().toString())
                        .url(linkurl)
                        .image(myImageUri);
                builder.show();
               /* view.getContext().startActivity(new Intent(view.getContext(),
                        TwitterShareActivity.class).putExtra("url", url));*/
                break;
            case R.id.view_more:
                if (cmtlist.size() > 0) {
                    showAllComments(context);
                }
                break;
        }
    }
/////////////////////

    private void showAllComments(final Context context) {
        final Dialog dialog1 = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog1.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.setContentView(R.layout.showallgossipcomments);
        TextView title1 = (TextView) dialog1.findViewById(R.id.title);
        final EditText cmts_hd = (EditText) dialog1.findViewById(R.id.cmts_hd);
        final ListView g_cmts_lv = (ListView) dialog1.findViewById(R.id.g_cmts_lv);
        final ImageView com_send = (ImageView) dialog1.findViewById(R.id.com_send);
        com_send.setVisibility(View.GONE);
      /*  if (InternetStatus.isConnectingToInternet(context)) {
            new GetCommentDetails(context, "facts", factList.get(FactDetailsActivity.currentpos).get("id").toString(),
                    total_cmmtLists.get(FactDetailsActivity.currentpos),g_cmts_lv).execute();
        } else {
            g_cmts_lv.setAdapter(new GossipCommentAdapter(context, total_cmmtLists.get(FactDetailsActivity.currentpos), true));
        }*/
        g_cmts_lv.setAdapter(gossipCommentAdapter);
        if (cmtlist.get(0).get("comment").equalsIgnoreCase("null")
                && cmtlist.get(0).get("user_title").equalsIgnoreCase("null")) {
            cmtlist.remove(0);
        }
        cmts_hd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    com_send.setVisibility(View.VISIBLE);
                } else {
                    com_send.setVisibility(View.GONE);

                }

            }
        });
        title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.cancel();
            }
        });
        cmts_hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCommentDialog(context);
                dialog1.cancel();
            }
        });
        dialog1.show();

    }


    ///////////////////////

    private class GetCommentDetails extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        String ctype, ctype_id;
        JSONParser jsonParser;
        HashMap<String, String> params = new HashMap<>();
        ArrayList<HashMap<String, String>> hashMaps;
        //time_format 2016-10-24 04:39:18
        ListView listView;

        public GetCommentDetails(Context context, String ctype, String ctype_id,
                                 ArrayList<HashMap<String, String>> hashMaps) {
            this.context = context;
            this.ctype = ctype;
            this.ctype_id = ctype_id;
            this.hashMaps = hashMaps;
            jsonParser = new JSONParser();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading  ....");
            //  pDialog.show();

        }

        protected JSONObject doInBackground(String... args) {
            params.put("comments", "");
            params.put("cate_id", ctype_id);
            params.put("user_type", ctype);
            JSONObject json = jsonParser.makeHttpRequest(Api.Get_Comments, "GET", params);
            if (json != null) {
                //Log.e("json response", ": " + json);
                return json;
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            // pDialog.dismiss();
            try {
                if (json.getString("status").equals("1")) {
                    //Log.e("current time", ": " + Util.currentDateTime());
                    JSONArray jsonArray = json.getJSONArray("items");
                    no_cmmts.setText(jsonArray.length() + " Comments");
                    hashMaps.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_title", jsonArray.getJSONObject(i).getString("title"));
                        map.put("comment", jsonArray.getJSONObject(i).getString("comments"));
                        map.put("date_time", jsonArray.getJSONObject(i).getString("date_time"));
                        map.put("cimg", jsonArray.getJSONObject(i).getString("img"));
                        hashMaps.add(map);
                    }
                    if (hashMaps.get(0).get("comment").equalsIgnoreCase("null")
                            || hashMaps.get(0).get("date_time").equalsIgnoreCase("null")) {
                        no_cmmts.setText("Comments not  available");
                        view_more_ly.setVisibility(View.GONE);
                        cmnt_p_ly.setVisibility(View.GONE);
                        cmnt_p_ly1.setVisibility(View.GONE);
                        cmnt_p_ly2.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                        divider1.setVisibility(View.GONE);

                    } else if (jsonArray.length() == 1) {
                        cn_name.setText(hashMaps.get(jsonArray.length() - 1).get("user_title"));
                        cmt_time.setText(hashMaps.get(jsonArray.length() - 1).get("date_time"));
                        cmtns_txv.setText(hashMaps.get(jsonArray.length() - 1).get("comment"));
                        Util.showImage(context, hashMaps.get(jsonArray.length() - 1).get("cimg"), cmt_img);
                        cmt_img_url = hashMaps.get(jsonArray.length() - 1).get("cimg");
                        cmnt_p_ly.setVisibility(View.VISIBLE);
                        view_more_ly.setVisibility(View.VISIBLE);


                    } else if (jsonArray.length() == 2) {
                        cn_name.setText(hashMaps.get(jsonArray.length() - 1).get("user_title"));
                        cmt_time.setText(hashMaps.get(jsonArray.length() - 1).get("date_time"));
                        cmtns_txv.setText(hashMaps.get(jsonArray.length() - 1).get("comment"));
                        Util.showImage(context, hashMaps.get(jsonArray.length() - 1).get("cimg"), cmt_img);
                        cmt_img_url = hashMaps.get(jsonArray.length() - 1).get("cimg");
                        cmnt_p_ly.setVisibility(View.VISIBLE);

                        cn_name1.setText(hashMaps.get(jsonArray.length() - 2).get("user_title"));
                        cmt_time1.setText(hashMaps.get(jsonArray.length() - 2).get("date_time"));
                        cmtns_txv1.setText(hashMaps.get(jsonArray.length() - 2).get("comment"));
                        Util.showImage(context, hashMaps.get(jsonArray.length() - 2).get("cimg"), cmt_img1);
                        cmt_img_url1 = hashMaps.get(jsonArray.length() - 2).get("cimg");
                        cmnt_p_ly1.setVisibility(View.VISIBLE);

                        view_more_ly.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);

                    } else if (jsonArray.length() >= 3) {
                        cn_name.setText(hashMaps.get(jsonArray.length() - 1).get("user_title"));
                        cmt_time.setText(hashMaps.get(jsonArray.length() - 1).get("date_time"));
                        cmtns_txv.setText(hashMaps.get(jsonArray.length() - 1).get("comment"));
                        Util.showImage(context, hashMaps.get(jsonArray.length() - 1).get("cimg"), cmt_img);
                        cmt_img_url = hashMaps.get(jsonArray.length() - 1).get("cimg");
                        cmnt_p_ly.setVisibility(View.VISIBLE);

                        cn_name1.setText(hashMaps.get(jsonArray.length() - 2).get("user_title"));
                        cmt_time1.setText(hashMaps.get(jsonArray.length() - 2).get("date_time"));
                        cmtns_txv1.setText(hashMaps.get(jsonArray.length() - 2).get("comment"));
                        Util.showImage(context, hashMaps.get(jsonArray.length() - 2).get("cimg"), cmt_img1);
                        cmt_img_url1 = hashMaps.get(jsonArray.length() - 2).get("cimg");
                        cmnt_p_ly1.setVisibility(View.VISIBLE);

                        cn_name2.setText(hashMaps.get(jsonArray.length() - 3).get("user_title"));
                        cmt_time2.setText(hashMaps.get(jsonArray.length() - 3).get("date_time"));
                        cmtns_txv2.setText(hashMaps.get(jsonArray.length() - 3).get("comment"));
                        Util.showImage(context, hashMaps.get(jsonArray.length() - 3).get("cimg"), cmt_img2);
                        cmt_img_url2 = hashMaps.get(jsonArray.length() - 3).get("cimg");
                        cmnt_p_ly2.setVisibility(View.VISIBLE);
                        view_more_ly.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);
                        divider1.setVisibility(View.VISIBLE);

                    }
                } else {
                    Toast.makeText(context, "Comments not available", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            new CheckUserLikeAsync(context, type, factslist.get(position).get("id")).execute();
        }
    }

    /////////////////////////////////////
    /////////////////get like details asynctask  for facts n gossip


    public class CheckUserLikeAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        String ctype, ctype_id;
        JSONParser jsonParser;
        HashMap<String, String> params = new HashMap<>();

        public CheckUserLikeAsync(Context context, String ctype, String ctype_id) {
            this.context = context;
            this.ctype = ctype;
            this.ctype_id = ctype_id;
            jsonParser = new JSONParser();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading  ....");
            //  pDialog.show();

        }

        protected JSONObject doInBackground(String... args) {
            params.put("u_id", MyPreference.loadUserid(context));
            params.put("type_id", ctype_id);
            params.put("type", ctype);
            JSONObject json = jsonParser.makeHttpRequest(Api.Checkuser_like_Url, "GET", params);
            if (json != null) {
                //Log.e("json response", ": " + json);
                return json;
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            Util.Set_Total_Like = 1;
            try {
                if (json != null && json.getString("status").equals("1")) {
                    if (!json.getString("like_status").equalsIgnoreCase("null")) {
                        // Util.User_Like = Integer.valueOf(json.getString("like_status"));
                        total_like = Integer.valueOf(json.getString("like_facts"));
                        check_Like(Integer.valueOf(json.getString("like_status")),
                                Integer.valueOf(json.getString("like_facts")));
                    }
                } else {
                    // Toast.makeText(context, "dnt get status", Toast.LENGTH_SHORT).show();
                    check_Like(0, Integer.valueOf(json.getString("like_facts")));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }


    /*  /////////////////////////*/
   /* //////////////addlike asynctask*/
    public class AddLikeAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        String ctype, ctype_id;
        JSONParser jsonParser;
        int like_staus;
        HashMap<String, String> params = new HashMap<>();

        public AddLikeAsync(Context context, String ctype, String ctype_id,
                            int like_staus) {
            this.context = context;
            this.ctype = ctype;
            this.ctype_id = ctype_id;
            this.like_staus = like_staus;
            jsonParser = new JSONParser();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading  ....");
            //  pDialog.show();

        }

        protected JSONObject doInBackground(String... args) {
            Util.Total_Like = 0;

            params.put("u_id", MyPreference.loadUserid(context));
            params.put("type_id", ctype_id);
            params.put("type", ctype);
            params.put("like_status", "" +like_staus);
            JSONObject json = jsonParser.makeHttpRequest(Api.Add_Like_Url, "GET", params);
            if (json != null) {
                //Log.e("json response", ": " + json);
                return json;
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            try {
                if (json != null && json.getString("status").equals("1")) {
                    if (!json.getString("total_like").equalsIgnoreCase("null")
                            && json.getString("msg").equalsIgnoreCase("unliked")) {
                        Util.User_Like = 0;
                        userlike = 0;
                        Util.Total_Like = Integer.valueOf(json.getString("total_like"));
                        like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart, 0, 0);
                        like.setTextColor(ContextCompat.getColor(context, R.color.colorRedt));
                        like.setText(Util.Total_Like + " Like");

                    } else if (!json.getString("total_like").equalsIgnoreCase("null")
                            && json.getString("msg").equalsIgnoreCase("liked")) {
                        Util.User_Like = 1;
                        userlike = 1;
                        Util.Total_Like = Integer.valueOf(json.getString("total_like"));
                        like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart_fill, 0, 0);
                        like.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        like.setText(Util.Total_Like + " Like");
                    } else {
                        Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                Toast.makeText(context, "Please try after some time", Toast.LENGTH_SHORT).show();
            }
        }

    }


    /*////////////////////*/
    /////////////////share content on facebook function
    private void shareOnFb(String title, String desc, String contenturl, String imageUrl) {
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setContentTitle(title)
                .setContentDescription(desc)
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.risein.suplla"))
                .setImageUrl(Uri.parse(imageUrl))
                .build();

        ShareDialog.show(getActivity(), shareLinkContent);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.Get_Comments == 1 && type.equalsIgnoreCase("facts")) {
            new GetCommentDetails(context, "facts", factslist.get(position).get("id"), cmtlist).execute();
        } else if (Util.Get_Comments == 1) {
            new GetCommentDetails(context, "gosip", factslist.get(position).get("id"), cmtlist).execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Util.Get_Comments = 0;
    }

    private void check_Like(int likestatus, int total_like) {
        if (likestatus == 1 && total_like != 0) {
            Util.User_Like = likestatus;
            userlike = likestatus;
            like.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart_fill, 0, 0);
            like.setText(total_like + " Like");

        } else {
            Util.User_Like = likestatus;
            userlike = likestatus;
            like.setTextColor(ContextCompat.getColor(context, R.color.colorRedt));
            like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart, 0, 0);
            like.setText(total_like + " Like");
        }
        Util.Total_Like = total_like;

    }

    ///////////////// add comment dialog
    ///////////////// add comment dialog
    private void addCommentDialog(final Context context) {
        final Dialog dialog1 = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog1.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.setContentView(R.layout.addcommentdialog);
        ImageView back_img = (ImageView) dialog1.findViewById(R.id.back_img);
        final EditText cmpt_box = (EditText) dialog1.findViewById(R.id.cmpt_box);
        Button send_cmt = (Button) dialog1.findViewById(R.id.send_cmt);
        Button cmt_cancel = (Button) dialog1.findViewById(R.id.cmt_cancel);
        dialog1.show();
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.cancel();
            }
        });

        send_cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    if (cmpt_box.getText().toString().isEmpty()) {
                        cmpt_box.setError(context.getString(R.string.empty_msg));
                    } else if (InternetStatus.isConnectingToInternet(context)) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_title", MyPreference.loadUsername(context));
                        map.put("comment", cmpt_box.getText().toString());
                        map.put("date_time", Util.currentDateTime());
                        map.put("cimg", MyPreference.loadUserPicUrl(context));
                        cmtlist.add(map);
                        gossipCommentAdapter.notifyDataSetChanged();
                        if (type.equalsIgnoreCase("facts")) {
                            new AddCommentAsync(context, "facts", factslist.get(FactDetailsActivity.currentpos).get("id").toString(),
                                    cmpt_box.getText().toString()).execute();
                            new GetCommentDetails(context, "facts", factslist.get(position).get("id"), cmtlist).execute();

                        } else {
                            new AddCommentAsync(context, "gosip", factslist.get(GossipActivity.currentpos).get("id").toString(),
                                    cmpt_box.getText().toString()).execute();
                            new GetCommentDetails(context, "gosip", factslist.get(position).get("id"), cmtlist).execute();

                        }
                        cmpt_box.setText("");
                        cmpt_box.setHint(context.getString(R.string.write_comment));
                        gossipCommentAdapter.notifyDataSetChanged();
                        //Log.e("comments list", ": " + cmtlist);

                    } else {
                    }

                    // dialog1.cancel();
                }
                dialog1.cancel();
            }
        });

        cmt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.cancel();

            }
        });


    }
    //////////////////////////////// convert image url to file

    public File String_to_File(String img_url) {
        File casted_image = null;
        try {
            File rootSdDirectory = Environment.getExternalStorageDirectory();

            casted_image = new File(rootSdDirectory, "attachment.jpg");
            if (casted_image.exists()) {
                casted_image.delete();
            }
            casted_image.createNewFile();

            FileOutputStream fos = new FileOutputStream(casted_image);

            URL url = new URL(img_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            InputStream in = connection.getInputStream();

            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = in.read(buffer)) > 0) {
                fos.write(buffer, 0, size);
            }
            fos.close();
            //Log.e("file", ": " + casted_image.getName());
            return casted_image;

        } catch (Exception e) {

            System.out.print(e);
            // e.printStackTrace();

        }
        return casted_image;
    }
}
