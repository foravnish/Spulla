package com.riseintech.spulla;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.riseintech.spulla.adapter.MatchSchedulePagerAdapter;

import com.riseintech.spulla.fragment.QuarterfinalFragment;
import com.riseintech.spulla.fragment.TouranmentTeamNamesfragments;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String POSITION = "POSITION";
    public static int currentpos;
    String team_size="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        ///adding divider betwen tab
       /* for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);
            Button tabTextView = (Button) relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            tab.select();
        }*/
    }

    private void setupViewPager(ViewPager viewPager) {
        MatchSchedulePagerAdapter adapter = new MatchSchedulePagerAdapter(MatchDetailActivity.this,
                getSupportFragmentManager());

        /*
        MatchFragment matchFragment1 = new MatchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("match_array", getIntent().getStringExtra("match_array"));
        matchFragment1.setArguments(bundle);

        KnockOutFragment knockOutFragment = new KnockOutFragment();
        Bundle knockbundle = new Bundle();
        knockbundle.putString("knckout_array", getIntent().getStringExtra("knckout_array"));
        knockOutFragment.setArguments(knockbundle);
        SemiFinalFragment semiFinalFragmentFragment = new SemiFinalFragment();
        Bundle bundle_semi = new Bundle();
        bundle_semi.putString("semifinal_array", getIntent().getStringExtra("semifinal_array"));
        semiFinalFragmentFragment.setArguments(bundle_semi);

        FinalFragment ff = new FinalFragment();
        Bundle ff_b = new Bundle();
        ff_b.putString("final_array", getIntent().getStringExtra("final_array"));
        ff.setArguments(ff_b);
        */
        QuarterfinalFragment matchFragment = new QuarterfinalFragment();
        Bundle quarterBundle1 = new Bundle();
        quarterBundle1.putString("quarter_final_array", getIntent().getStringExtra("quarter_final_array"));
        matchFragment.setArguments(quarterBundle1);

        TouranmentTeamNamesfragments ttn_frag = new TouranmentTeamNamesfragments();
        Bundle ttn_b = new Bundle();
        ttn_b.putString("tname_array", getIntent().getStringExtra("tname_array"));
        team_size = getIntent().getStringExtra("team_size");
        ttn_frag.setArguments(ttn_b);


        if (team_size.equalsIgnoreCase("null") || team_size.matches("") || team_size.equalsIgnoreCase("0")) {
            try {
                adapter.addFragment(matchFragment, "Teams Name", parseTeamNmaeJsonArray(new JSONArray(getIntent().getStringExtra("tname_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("final_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("final_array"))));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (team_size.equalsIgnoreCase("2")) {
            try {
                adapter.addFragment(ttn_frag, "Teams Name", parseTeamNmaeJsonArray(new JSONArray(getIntent().getStringExtra("tname_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("final_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("final_array"))));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (team_size.equalsIgnoreCase("4")) {
            try {
                adapter.addFragment(ttn_frag, "Teams Name", parseTeamNmaeJsonArray(new JSONArray(getIntent().getStringExtra("tname_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("semifinal_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("semifinal_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("final_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("final_array"))));

            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* adapter.addFragment(ttn_frag, "Teams Name");
            adapter.addFragment(matchFragment, "Match");
            adapter.addFragment(ff, "Final");*/
        } else if (team_size.equalsIgnoreCase("8")) {
            try {
                adapter.addFragment(ttn_frag, "Teams Name", parseTeamNmaeJsonArray(new JSONArray(getIntent().getStringExtra("tname_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("quarter_final_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("quarter_final_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("semifinal_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("semifinal_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("final_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("final_array"))));

            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* adapter.addFragment(ttn_frag, "Teams Name");
            adapter.addFragment(matchFragment, "Match");
            adapter.addFragment(semiFinalFragmentFragment, "SemiFinal");
            adapter.addFragment(ff, "Final");*/
        } else if (team_size.equalsIgnoreCase("16")) {
            try {
                adapter.addFragment(ttn_frag, "Teams Name", parseTeamNmaeJsonArray(new JSONArray(getIntent().getStringExtra("tname_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("knckout_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("knckout_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("quarter_final_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("quarter_final_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("semifinal_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("semifinal_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("final_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("final_array"))));

            } catch (JSONException e) {
                e.printStackTrace();
            }
          /*  adapter.addFragment(ttn_frag, "Teams Name");
            adapter.addFragment(matchFragment, "Match");
            adapter.addFragment(new QuarterfinalFragment(), "Quater Final");
            adapter.addFragment(semiFinalFragmentFragment, "SemiFinal");
            adapter.addFragment(ff, "Final");*/
        } else if (team_size.equalsIgnoreCase("32")) {
            try {
                adapter.addFragment(ttn_frag, "Teams Name", parseTeamNmaeJsonArray(new JSONArray(getIntent().getStringExtra("tname_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("match_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("match_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("knckout_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("knckout_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("quarter_final_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("quarter_final_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("semifinal_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("semifinal_array"))));
                adapter.addFragment(matchFragment, getTitleNmae(new JSONArray(getIntent().getStringExtra("final_array"))), parematchJsonArray(new JSONArray(getIntent().getStringExtra("final_array"))));

            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* adapter.addFragment(ttn_frag, "Teams Name");
            adapter.addFragment(matchFragment, "Match");
            adapter.addFragment(knockOutFragment, "Super 16");
            adapter.addFragment(quarterfinalFragment, "Quater Final");
            adapter.addFragment(semiFinalFragmentFragment, "SemiFinal");
            adapter.addFragment(ff, "Final");*/
        }
       /* adapter.addFragment(ttn_frag, "Teams Name");
        adapter.addFragment(matchFragment, "Match");
        adapter.addFragment(semiFinalFragmentFragment, "SemiFinal");
        adapter.addFragment(ff, "Final");

  */
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }


    private ArrayList<HashMap<String, String>> parematchJsonArray(JSONArray final_array) {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        if (final_array != null) {
            {
                for (int i = 0; i < final_array.length(); i++) {
                    HashMap<String, String> map = new HashMap<>();
                    try {
                        map.put("id", final_array.getJSONObject(i).optString("id"));
                        map.put("team1_icon", final_array.getJSONObject(i).optString("team1_icon"));
                        map.put("team_name1", final_array.getJSONObject(i).optString("team_name1"));
                        map.put("team2_icon", final_array.getJSONObject(i).optString("team2_icon"));
                        map.put("team_name2", final_array.getJSONObject(i).optString("team_name2"));
                        if (final_array.getJSONObject(i).optString("date_time").contains(" ")) {
                            String s[] = final_array.getJSONObject(i).optString("date_time").split(" ");
                            map.put("date", s[0]);
                            map.put("time", s[1]);
                        } else {
                            map.put("date", "Not Available");
                            map.put("time", "Not Available");
                        }
                        map.put("location", final_array.getJSONObject(i).optString("location"));
                        map.put("winner", final_array.getJSONObject(i).optString("winner"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    arrayList.add(map);
                }
            }
        }

        return arrayList;
    }

    private ArrayList<HashMap<String, String>> parseTeamNmaeJsonArray(JSONArray teamName) {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        if (teamName != null) {
            {
                for (int i = 0; i < teamName.length(); i++) {
                    boolean isBlank = false;
                    HashMap<String, String> map = new HashMap<>();

                    try {
                        map.put("id", teamName.getJSONObject(i).getString("id"));
                        map.put("team_name", teamName.getJSONObject(i).optString("team_name"));
                        map.put("team_icon", teamName.getJSONObject(i).optString("team_icon"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    arrayList.add(map);
                }
            }
        }

        return arrayList;
    }

    private String getTitleNmae(JSONArray jsonArray) {
        String title = "Not Available";
        if (jsonArray != null) {
            try {
                if (jsonArray.getJSONObject(0).optString("match_level_name").matches("")
                        || jsonArray.getJSONObject(0).optString("match_level_name").matches("null")
                        || jsonArray.getJSONObject(0).optString("match_level_name").matches("undefined")) {
                    title = "";
                } else {
                    title = jsonArray.getJSONObject(0).optString("match_level_name");

                }
            } catch (JSONException e) {
                e.printStackTrace();
                title = "";
            }
        }

        return title;
    }
}
