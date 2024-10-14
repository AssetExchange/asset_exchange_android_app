package com.example.assetexdemo1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileSharingOptionsActivity extends AppCompatActivity {
    private ImageButton backProfileSharingOptions;
    private RadioGroup profileSharingOptionsRadioGroup;
    private Button profileSharingOptionsShareButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_sharing_options);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backProfileSharingOptions = findViewById(R.id.backProfileSharingOptions);
        profileSharingOptionsRadioGroup = findViewById(R.id.profileSharingOptionsRadioGroup);
        profileSharingOptionsShareButton = findViewById(R.id.profileSharingOptionsShareButton);
        progressBar = findViewById(R.id.progressBar13);

        backProfileSharingOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        profileSharingOptionsShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}