package lk.mobile.meghanaada;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import lk.mobile.meghanaada.adapter.ContactsAdapter;
import lk.mobile.meghanaada.adapter.SportAdapter;
import lk.mobile.meghanaada.model.Contact;
import lk.mobile.meghanaada.model.Sport;
import lk.mobile.meghanaada.utils.DividerItemDecoration;

public class MainActivity extends AppCompatActivity implements SportAdapter.Callback, ContactsAdapter.ContactsAdapterListener {

    Button btnMaps;

    RecyclerView mRecyclerView;
    SportAdapter mSportAdapter;

    LinearLayoutManager mLayoutManager;
    ArrayList<Sport> mSports;
    private EditText searchView;
    private List<Contact> contactList;
    private ContactsAdapter mAdapter;
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
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapter(this, contactList, this);

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

        contactList.clear();

        Contact contact = new Contact("Thimbirigasyaya", "Colombo", "Normal", "2.3", "Kelani River", "0.1", "-");
        contactList.add(contact);
        contact = new Contact("Dehiwala", "Colombo", "Normal", "2.3", "Kelani River", "0.1", "-");
        contactList.add(contact);
        contact = new Contact("Moratuwa", "Colombo", "Normal", "3	", "	Bolgoda Lake", "0.1", "-");
        contactList.add(contact);
        contact = new Contact("Kotte", "	Colombo", "Normal", "2.3", "	Kelani River", "0.1", "-");
        contactList.add(contact);
        contact = new Contact("Negombo", "Gampaha", "Normal", "1.6", "Maha Oya", "0.1", "-");
        contactList.add(contact);
        contact = new Contact("Kandy", "Kandy", "Normal", "3.7", "Mahaweli", "0.1", "-");
        contactList.add(contact);
        contact = new Contact("Sainthamarathu", "Ampara", "Normal", "2.3", "Kelani River", "0.1", "-");
        contactList.add(contact);
        contact = new Contact("Vavuniya", "Vavuniya", "Normal", "2.3", "Kelani River", "0.1", "-");
        contactList.add(contact);
        contact = new Contact("Galle", "Galle", "Normal", "3", "Bolgoda Lake", "0.1", "-");
        contactList.add(contact);
        contact = new Contact("Trincomalee Town and Gravets", "Trincomalee	", "	Normal	", "	2.3	", "	Kelani River	", "	0.1	", "	-	");
        contactList.add(contact);
        contact = new Contact("Manmunai North	", "Batticaloa	", "Normal	", "	1.6	", "	Maha Oya	", "	0.1	", "	-	");
        contactList.add(contact);
        contact = new Contact("Nallur	", "Jaffna	", "Normal	", "	3.7	", "	Mahaweli	", "	0.1	", "	-	");
        contactList.add(contact);
        contact = new Contact("Katana	", "Gampaha	", "Normal	", "	2.3	", "	Kelani River	", "	0.1	", "	-	");
        contactList.add(contact);
        contact = new Contact("Dambulla", "Matale	", "Normal	", "	2.3	", "	Kelani River	", "	0.1	", "	-	");
        contactList.add(contact);
        contact = new Contact("Kolonnawa", "Colombo	", "Normal	", "	3	", "	Bolgoda Lake	", "	0.1	", "	-	");
        contactList.add(contact);
        contact = new Contact("Anuradhapura", "Anuradhapura	", "Normal	", "	2.3	", "	Kelani River	", "	0.1	", "	-	");
        contactList.add(contact);
        contact = new Contact("Ratnapura", "Ratnapura	", "Normal	", "	1.6	", "	Maha Oya	", "	0.1	", "	-	");
        contactList.add(contact);

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
    public void onContactSelected(Contact contact) {
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

        txtLocation.setText(contact.getName());
        txtDistrict.setText(contact.getDistrict());
        txtRiskStatus.setText(contact.getRisk());
        txtNearestRiver.setText(contact.getWater_level_near_river());
        txtWaterLevel.setText(contact.getWater_level());
        txtIncreased.setText(contact.getIncreased_water_level());
        txtSafe.setText(contact.getSafest_location_near());

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

        mSports = new ArrayList<>();
        mSportAdapter = new SportAdapter(mSports);

        prepareDemoContent();
    }

    private void prepareDemoContent() {
        mSports = new ArrayList<>();
        String[] sportsList = getResources().getStringArray(R.array.sports_titles);
        String[] sportsInfo = getResources().getStringArray(R.array.sports_info);
        String[] sportsSub = getResources().getStringArray(R.array.sports_sub_titles);
        for (int i = 0; i < sportsList.length; i++) {
            mSports.add(new Sport(sportsInfo[i], sportsSub[i], sportsList[i]));
        }
        mSportAdapter.addItems(mSports);
        mRecyclerView.setAdapter(mSportAdapter);


    }

    @Override
    public void onEmptyViewRetryClick() {
        prepareDemoContent();
    }
}
