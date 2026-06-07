package com.example.numberbook.ui;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;

import com.example.numberbook.R;
import com.example.numberbook.model.ApiResponse;
import com.example.numberbook.model.Contact;
import com.example.numberbook.network.ContactApi;
import com.example.numberbook.network.RetrofitClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnLoadContacts;
    private Button btnSyncContacts;
    private Button btnShowServerContacts;
    private Button btnSearch;
    private EditText etKeyword;
    private RecyclerView recyclerViewContacts;

    private ContactAdapter adapter;
    private final List<Contact> contactList = new ArrayList<>();
    private ContactApi contactApi;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            loadContacts();
                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    getString(R.string.permission_denied),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoadContacts = findViewById(R.id.btnLoadContacts);
        btnSyncContacts = findViewById(R.id.btnSyncContacts);
        btnShowServerContacts = findViewById(R.id.btnShowServerContacts);
        btnSearch = findViewById(R.id.btnSearch);
        etKeyword = findViewById(R.id.etKeyword);
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);

        adapter = new ContactAdapter();

        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewContacts.setAdapter(adapter);

        contactApi = RetrofitClient.getClient().create(ContactApi.class);

        btnLoadContacts.setOnClickListener(view -> checkPermissionAndLoadContacts());
        btnSyncContacts.setOnClickListener(view -> syncContactsToServer());
        btnShowServerContacts.setOnClickListener(view -> loadContactsFromServer());
        btnSearch.setOnClickListener(view -> searchContacts());
    }

    private void checkPermissionAndLoadContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private void loadContacts() {
        contactList.clear();

        Map<String, Contact> uniqueContacts = new LinkedHashMap<>();

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        )
                );

                String phone = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                );

                name = cleanName(name);
                phone = cleanPhone(phone);

                if (!name.isEmpty() && !phone.isEmpty()) {
                    uniqueContacts.put(phone, new Contact(name, phone));
                }
            }

            cursor.close();
        }

        contactList.addAll(uniqueContacts.values());
        adapter.updateData(contactList);

        Toast.makeText(
                this,
                getString(R.string.contacts_loaded, contactList.size()),
                Toast.LENGTH_SHORT
        ).show();
    }

    private String cleanName(String name) {
        if (name == null) {
            return "";
        }

        return name.trim();
    }

    private String cleanPhone(String phone) {
        if (phone == null) {
            return "";
        }

        return phone.trim()
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");
    }

    private void syncContactsToServer() {
        if (contactList.isEmpty()) {
            Toast.makeText(
                    this,
                    getString(R.string.empty_contacts),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Toast.makeText(
                this,
                getString(R.string.sync_started),
                Toast.LENGTH_SHORT
        ).show();

        final int total = contactList.size();
        final int[] completed = {0};
        final int[] success = {0};
        final int[] errors = {0};

        for (Contact contact : contactList) {
            contactApi.insertContact(contact).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(
                        @NonNull Call<ApiResponse> call,
                        @NonNull Response<ApiResponse> response) {

                    completed[0]++;

                    if (response.isSuccessful()
                            && response.body() != null
                            && response.body().isSuccess()) {
                        success[0]++;
                    } else {
                        errors[0]++;
                    }

                    showSyncResultIfFinished(completed[0], total, success[0], errors[0]);
                }

                @Override
                public void onFailure(
                        @NonNull Call<ApiResponse> call,
                        @NonNull Throwable t) {

                    completed[0]++;
                    errors[0]++;

                    showSyncResultIfFinished(completed[0], total, success[0], errors[0]);
                }
            });
        }
    }

    private void showSyncResultIfFinished(int completed, int total, int success, int errors) {
        if (completed == total) {
            Toast.makeText(
                    this,
                    getString(R.string.sync_done, success, errors),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void loadContactsFromServer() {
        contactApi.getAllContacts().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<Contact>> call,
                    @NonNull Response<List<Contact>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Contact> serverContacts = response.body();
                    adapter.updateData(serverContacts);

                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.contacts_from_server, serverContacts.size()),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.server_contacts_error),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<Contact>> call,
                    @NonNull Throwable t) {

                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.network_error),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void searchContacts() {
        String keyword = etKeyword.getText().toString().trim();

        if (keyword.isEmpty()) {
            Toast.makeText(
                    this,
                    getString(R.string.search_empty),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        contactApi.searchContacts(keyword).enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<Contact>> call,
                    @NonNull Response<List<Contact>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                } else {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.search_error),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<Contact>> call,
                    @NonNull Throwable t) {

                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.network_error),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}