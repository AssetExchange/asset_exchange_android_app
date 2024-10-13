package com.example.assetexdemo1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;

import java.util.HashMap;
import java.util.Map;

public class ProfileViewerActivity extends AppCompatActivity {
    TextView profileViewerName;
    TextView profileViewerEmail;
    ImageButton profileViewerProfilePicture;
    ImageButton profileViewerBackButton, profileViewerEditButton;
    EditText profileViewerEditName;
    Button profileViewerSaveChangesButton;
    Chip profileViewerRoleChip;
    ProgressBar progressBar;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_viewer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent() != null) {
            if (getIntent().hasExtra("user_model")) {
                userModel = getIntent().getParcelableExtra("user_model");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userModel != null) {
            profileViewerName = findViewById(R.id.profileViewerName);
            profileViewerEmail = findViewById(R.id.profileViewerEmail);
            profileViewerProfilePicture = findViewById(R.id.profileViewerProfilePicture);
            profileViewerBackButton = findViewById(R.id.profileViewerBackButton);
            profileViewerEditButton = findViewById(R.id.profileViewerEditButton);
            profileViewerEditName = findViewById(R.id.profileViewerEditName);
            profileViewerRoleChip = findViewById(R.id.profileViewerRoleChip);
            profileViewerSaveChangesButton = findViewById(R.id.profileViewerSaveChangesButton);
            progressBar = findViewById(R.id.progressBar5);

            SharedPreferences sharedPref = AssetExchangeApp.context.getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);
            // profileViewerName.setText(sharedPref.getString("full_name", "Your Name"));
            // profileViewerEmail.setText(sharedPref.getString("email", "Email Address"));

            String[] roles = new String[] {
                "Admin",
                "Client",
                "Designer",
                "Freelancer",
                "UI/UX Designer",
                "Graphic Designer",
                "Video Editor",
                "Social Media Manager",
                "Web Designer",
                "Frontend Dev",
                "Backend Dev",
                "Project Manager",
            };

            profileViewerName.setText(userModel.getFullName());
            profileViewerEmail.setText(userModel.getEmail());
            profileViewerRoleChip.setText(roles[userModel.getRoleId() - 1]);

            progressBar.setVisibility(View.GONE);

            if (userModel.getProfilePicPath() != null) {
                String ext = userModel.getProfilePicPath().trim().toLowerCase();
                if (ext.endsWith(".png") || ext.endsWith(".jpg") || ext.endsWith(".jpeg") || ext.endsWith(".gif")) {
                    Glide.with(this)
                        .load(DBConn.getURL("filegator/repository/user/" + userModel.getProfilePicPath()))
                        .apply(
                            new RequestOptions()
                            .placeholder(R.drawable.bottom_nav_menu_profile_icon)
                        )
                        .fitCenter()
                        .dontAnimate()
                        .into(profileViewerProfilePicture);
                }
            }


            profileViewerBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            if (userModel.getUserId() != Integer.parseInt(sharedPref.getString("user_id", "1"))) {
                profileViewerEditButton.setVisibility(View.GONE);
            }

            profileViewerEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userModel.getUserId() == Integer.parseInt(sharedPref.getString("user_id", "1"))) {
                        profileViewerSaveChangesButton.setVisibility(View.VISIBLE);
                        profileViewerEditName.setVisibility(View.VISIBLE);
                        profileViewerName.setVisibility(View.GONE);

                        profileViewerEditName.setText(profileViewerName.getText().toString());
                    }

                }
            });

            profileViewerSaveChangesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> params = new HashMap<>();

                    params.put("action", "update_profile_data");
                    params.put("no_image", "true");
                    params.put("user_id", String.valueOf(userModel.getUserId()));
                    params.put("full_name", profileViewerEditName.getText().toString());

                    if (!profileViewerEditName.getText().toString().isBlank()) {
                        progressBar.setVisibility(View.VISIBLE);

                        DBConn.postRequest(
                            DBConn.getURL("update_profile.php"),
                            ProfileViewerActivity.this,
                            params,
                            new DBConn.ResponseCallback() {
                                @Override
                                public void innerResponse(Object object) {
                                    progressBar.setVisibility(View.GONE);

                                    if (object.equals("Profile updated successfully.")) {
                                        userModel.setFullName(profileViewerEditName.getText().toString());

                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("full_name", profileViewerEditName.getText().toString()).commit();

                                        profileViewerName.setText(profileViewerEditName.getText().toString());
                                    }
                                    else {
                                        profileViewerEditName.setText(userModel.getFullName());
                                    }
                                }
                            },
                            "Unable to connect to the database",
                            "Unable to parse API response"
                        );

                        profileViewerName.setVisibility(View.VISIBLE);
                        profileViewerEditName.setVisibility(View.GONE);
                        profileViewerSaveChangesButton.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(ProfileViewerActivity.this, "Name cannot be blank", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }



    }
}