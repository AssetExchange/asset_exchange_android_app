package com.example.assetexdemo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AssetRevisionActivity extends AppCompatActivity {
    AssetModel assetModel = null;

    ImageButton backAssetRevision;
    ImageView assetRevisionLatestRevisionImage;
    Button assetRevisionAddRevisionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asset_revision);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backAssetRevision = findViewById(R.id.backAssetRevision);
        assetRevisionLatestRevisionImage = findViewById(R.id.assetRevisionLatestRevisionImage);
        assetRevisionAddRevisionButton = findViewById(R.id.assetRevisionAddRevisionButton);

        assetModel = getIntent().getParcelableExtra("asset_model");

        backAssetRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assetModel != null) {
                    Intent intent = getIntent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
                else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (assetModel != null) {
            Glide.with(this)
                .load(DBConn.getURL("filegator/repository/user/" + assetModel.getLatestRevisionFilePath()))
                .apply(
                    new RequestOptions()
                    .placeholder(android.R.drawable.screen_background_dark)
                    .error(R.drawable.password_cross)
                )
                .centerInside()
                .dontAnimate()
                .into(assetRevisionLatestRevisionImage);

            assetRevisionAddRevisionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();

                    bundle.putParcelable("asset_model", assetModel);
                    AddRevisionBottomSheet bottomSheet = new AddRevisionBottomSheet();

                    bottomSheet.setArguments(bundle);
                    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
                }
            });
        }
    }
}