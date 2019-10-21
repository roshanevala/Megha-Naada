package lk.mobile.meghanaada;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lk.mobile.meghanaada.adapter.RiskAdapter;
import lk.mobile.meghanaada.adapter.NewsAdapter;
import lk.mobile.meghanaada.model.Risk;
import lk.mobile.meghanaada.model.News;
import lk.mobile.meghanaada.utils.DividerItemDecoration;

public class MainActivity extends AppCompatActivity implements NewsAdapter.Callback, RiskAdapter.ContactsAdapterListener {

    Button btnMaps;

    RecyclerView mRecyclerView;
    NewsAdapter mNewsAdapter;

    LinearLayoutManager mLayoutManager;
    ArrayList<News> mNews;
    private EditText searchView;
    private List<Risk> riskList;
    private RiskAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        btnMaps = (Button) findViewById(R.id.btn_maps);
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        setUp();

        recyclerView = findViewById(R.id.recycler_view);
        riskList = new ArrayList<>();
        mAdapter = new RiskAdapter(this, riskList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawable);

        recyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        recyclerView.setAdapter(mAdapter);

        fetchContacts();

        searchView = (EditText) findViewById(R.id.edit_search);
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    mAdapter.getFilter().filter(v.getText());
                    return true;
                }
                return false;
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                mAdapter.getFilter().filter(s);
            }
        });
    }

    private void fetchContacts() {

        riskList.clear();

        Risk risk = new Risk("Thimbirigasyaya", "Colombo", "Normal", "2.3", "Kelani River", "0.1", "-");
        riskList.add(risk);
        risk = new Risk("Dehiwala", "Colombo", "Normal", "2.3", "Kelani River", "0.1", "-");
        riskList.add(risk);
        risk = new Risk("Moratuwa", "Colombo", "Normal", "3	", "	Bolgoda Lake", "0.1", "-");
        riskList.add(risk);
        risk = new Risk("Kotte", "	Colombo", "Normal", "2.3", "	Kelani River", "0.1", "-");
        riskList.add(risk);
        risk = new Risk("Negombo", "Gampaha", "Normal", "1.6", "Maha Oya", "0.1", "-");
        riskList.add(risk);
        risk = new Risk("Kandy", "Kandy", "Normal", "3.7", "Mahaweli", "0.1", "-");
        riskList.add(risk);
        risk = new Risk("Sainthamarathu", "Ampara", "Normal", "2.3", "Kelani River", "0.1", "-");
        riskList.add(risk);
        risk = new Risk("Vavuniya", "Vavuniya", "Normal", "2.3", "Kelani River", "0.1", "-");
        riskList.add(risk);
        risk = new Risk("Galle", "Galle", "Normal", "3", "Bolgoda Lake", "0.1", "-");
        riskList.add(risk);
        risk = new Risk("Trincomalee Town and Gravets", "Trincomalee	", "	Normal	", "	2.3	", "	Kelani River	", "	0.1	", "	-	");
        riskList.add(risk);
        risk = new Risk("Manmunai North	", "Batticaloa	", "Normal	", "	1.6	", "	Maha Oya	", "	0.1	", "	-	");
        riskList.add(risk);
        risk = new Risk("Nallur	", "Jaffna	", "Normal	", "	3.7	", "	Mahaweli	", "	0.1	", "	-	");
        riskList.add(risk);
        risk = new Risk("Katana	", "Gampaha	", "Normal	", "	2.3	", "	Kelani River	", "	0.1	", "	-	");
        riskList.add(risk);
        risk = new Risk("Dambulla", "Matale	", "Normal	", "	2.3	", "	Kelani River	", "	0.1	", "	-	");
        riskList.add(risk);
        risk = new Risk("Kolonnawa", "Colombo	", "Normal	", "	3	", "	Bolgoda Lake	", "	0.1	", "	-	");
        riskList.add(risk);
        risk = new Risk("Anuradhapura", "Anuradhapura	", "Normal	", "	2.3	", "	Kelani River	", "	0.1	", "	-	");
        riskList.add(risk);
        risk = new Risk("Ratnapura", "Ratnapura	", "Normal	", "	1.6	", "	Maha Oya	", "	0.1	", "	-	");
        riskList.add(risk);

        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        // close search view on back button pressed

        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(Risk risk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.aler_dialog, viewGroup, false);
        builder.setView(dialogView);

        final TextView txtLocation = (TextView) dialogView.findViewById(R.id.location);
        final TextView txtDistrict = (TextView) dialogView.findViewById(R.id.district);
        final TextView txtRiskStatus = (TextView) dialogView.findViewById(R.id.risk_status);
        final TextView txtNearestRiver = (TextView) dialogView.findViewById(R.id.nearest_river);
        final TextView txtWaterLevel = (TextView) dialogView.findViewById(R.id.water_level);
        final TextView txtIncreased = (TextView) dialogView.findViewById(R.id.increased_water_level);
        final TextView txtSafe = (TextView) dialogView.findViewById(R.id.nearest_safe_location);

        txtLocation.setText(risk.getName());
        txtDistrict.setText(risk.getDistrict());
        txtRiskStatus.setText(risk.getRisk());
        txtNearestRiver.setText(risk.getWater_level_near_river());
        txtWaterLevel.setText(risk.getWater_level());
        txtIncreased.setText(risk.getIncreased_water_level());
        txtSafe.setText(risk.getSafest_location_near());

        builder.setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setUp() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawable);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));

        mNews = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(mNews);

        prepareDemoContent();
    }

    private void prepareDemoContent() {
        mNews = new ArrayList<>();
        String[] sportsList = getResources().getStringArray(R.array.sports_titles);
        String[] sportsInfo = getResources().getStringArray(R.array.sports_info);
        String[] sportsSub = getResources().getStringArray(R.array.sports_sub_titles);
        for (int i = 0; i < sportsList.length; i++) {
            mNews.add(new News(sportsInfo[i], sportsSub[i], sportsList[i]));
        }
        mNewsAdapter.addItems(mNews);
        mRecyclerView.setAdapter(mNewsAdapter);


    }

    @Override
    public void onEmptyViewRetryClick() {
        prepareDemoContent();
    }
}
