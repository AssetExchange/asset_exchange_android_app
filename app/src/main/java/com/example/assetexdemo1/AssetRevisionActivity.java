package com.example.assetexdemo1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.otaliastudios.zoom.ZoomLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AssetRevisionActivity extends AppCompatActivity {
    AssetModel assetModel = null;

    ImageButton backAssetRevision;
    Button assetRevisionAddRevisionButton;
    ImageView assetRevisionLatestRevisionImage;

    TextView assetRevisionAssetDateUploaded, assetRevisionAssetDateModified, assetRevisionAssetDescription, assetRevisionAssetName;

    EditText assetRevisionAddCommentEditText;
    ImageButton assetRevisionAddCommentButton;
    LinearLayout assetRevisionAddCommentLinearLayout;
    Button assetRevisionRevertButton;

    ProgressBar progressBar;

    RecyclerView assetRevisionRV;
    int keyboardHeight = 0;
    boolean isVisible = false;

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

        assetRevisionAssetDateUploaded = findViewById(R.id.assetRevisionAssetDateUploaded);
        assetRevisionAssetDateModified = findViewById(R.id.assetRevisionAssetDateModified);
        assetRevisionAssetDescription = findViewById(R.id.assetRevisionAssetDescription);
        assetRevisionAssetName = findViewById(R.id.assetRevisionAssetName);

        assetRevisionAddCommentEditText = findViewById(R.id.assetRevisionAddCommentEditText);
        assetRevisionAddCommentButton = findViewById(R.id.assetRevisionAddCommentButton);
        assetRevisionAddCommentLinearLayout = findViewById(R.id.assetRevisionAddCommentLinearLayout);

        assetRevisionRevertButton = findViewById(R.id.assetRevisionRevertButton);

        progressBar = findViewById(R.id.progressBar15);

        assetModel = getIntent().getParcelableExtra("asset_model");

        assetRevisionRV = findViewById(R.id.assetRevisionRecyclerView);

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

        findViewById(R.id.main).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                findViewById(R.id.main).getWindowVisibleDisplayFrame(rect);

                keyboardHeight = findViewById(R.id.main).getRootView().getHeight() - rect.height();

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) assetRevisionAddCommentLinearLayout.getLayoutParams();
                if (keyboardHeight > 200) {
                    isVisible = true;
                    params.setMargins(20, 0, 20, keyboardHeight - 26);

                } else {
                    isVisible = false;
                    params.setMargins(20, 0, 20, 26);
                }
                assetRevisionAddCommentLinearLayout.setLayoutParams(params);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (assetModel != null) {
            String ext = assetModel.getLatestRevisionFilePath().trim().toLowerCase();

            if (ext.endsWith(".png") || ext.endsWith(".jpg") || ext.endsWith(".jpeg") || ext.endsWith(".gif")) {
                Glide.with(this)
                    .load(DBConn.getURL("filegator/repository/user/" + assetModel.getLatestRevisionFilePath()))
                    .apply(
                        new RequestOptions()
                        .placeholder(android.R.drawable.screen_background_dark)
                        .error(R.drawable.password_cross)
                    )
                    .centerInside()
                    .transform(new RoundedCorners(20))
                    .dontAnimate()
                    .into(assetRevisionLatestRevisionImage);
            }
            else {
                assetRevisionLatestRevisionImage.setVisibility(View.GONE);
            }

            SharedPreferences sharedPref = AssetExchangeApp.context.getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

            if (sharedPref.getString("role_id", "4").equals("2")) {
                assetRevisionAddRevisionButton.setVisibility(View.GONE);
                assetRevisionRevertButton.setVisibility(View.GONE);
                assetRevisionAddCommentLinearLayout.setVisibility(View.VISIBLE);
            }
            else {
                assetRevisionAddRevisionButton.setVisibility(View.VISIBLE);
                assetRevisionRevertButton.setVisibility(View.VISIBLE);
                assetRevisionAddCommentLinearLayout.setVisibility(View.GONE);
            }

            assetRevisionAddCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (assetRevisionAddCommentEditText.getText().toString().trim().isEmpty()) {
                        Toast.makeText(AssetRevisionActivity.this, "Please add a comment", Toast.LENGTH_LONG).show();
                    }
                    else {
                        SharedPreferences sharedPref = AssetExchangeApp.context.getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

                        HashMap<String, String> params = new HashMap<>();

                        params.put("action", "add_comment");
                        params.put("asset_id", String.valueOf(assetModel.getAssetId()));
                        params.put("revision_comment_user_id", sharedPref.getString("user_id", "1"));
                        params.put("revision_comment_text", assetRevisionAddCommentEditText.getText().toString());

                        DBConn.postRequest(
                            DBConn.getURL("add_comment.php"),
                            AssetRevisionActivity.this,
                            params,
                            new DBConn.ResponseCallback() {
                                @Override
                                public void innerResponse(Object object) {
                                    Toast.makeText(AssetRevisionActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
                                }
                            },
                        "Unable to connect to the database",
                        "Unable to parse API response"
                        );
                    }
                }
            });

            assetRevisionAssetDateUploaded.setText(assetModel.getDateCreated().format(DateTimeFormatter.ofPattern("LLLL d, yyyy h:mm:ss a")));
            assetRevisionAssetDateModified.setText(assetModel.getDateModified().format(DateTimeFormatter.ofPattern("LLLL d, yyyy h:mm:ss a")));

            if (assetModel.getAssetDescription() == null || assetModel.getAssetDescription().isEmpty()) {
                assetRevisionAssetDescription.setText("No description provided.");
                assetRevisionAssetDescription.setTypeface(null, Typeface.ITALIC);
            }
            else {
                assetRevisionAssetDescription.setText(assetModel.getAssetDescription());
            }

            if (assetModel.getDateCreated().isEqual(assetModel.getDateModified()))  {
                assetRevisionAssetDateModified.setText("");
            }

            assetRevisionAssetName.setText(assetModel.getAssetTitle());

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

            assetRevisionRevertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AssetRevisionActivity.this);

                    builder.setTitle("Revert Asset Confirmation")
                        .setMessage("Are you sure you want to revert this asset? This cannot be undone.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                Map<String, String> params = new HashMap<>();

                                params.put("asset_revision_id", String.valueOf(assetModel.getAssetId()));

                                DBConn.postRequest(
                                        DBConn.getURL("revert_asset.php"),
                                        AssetRevisionActivity.this,
                                        params,
                                        new DBConn.ResponseCallback() {
                                            @Override
                                            public void innerResponse(Object object) {
                                                System.out.println(object.getClass());
                                                if (object instanceof JSONObject) {
                                                    try {
                                                        Toast.makeText(AssetRevisionActivity.this, ((JSONObject) object).getString("code"), Toast.LENGTH_LONG).show();
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                                else if (object instanceof String) {
                                                    Toast.makeText(AssetRevisionActivity.this, (String) object, Toast.LENGTH_LONG).show();
                                                }
                                                System.out.println(object.toString());
                                            }
                                        },
                                        "aaaa",
                                        "bbbb"
                                );
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                    );
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


            ArrayList<AssetVersionModel> assetVersionModels = new ArrayList<>();
            AssetRevisionVersionHistoryAdapter assetRevisionVersionHistoryAdapter = new AssetRevisionVersionHistoryAdapter(this, assetModel, assetVersionModels);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            assetRevisionRV.setLayoutManager(linearLayoutManager);
            assetRevisionRV.setAdapter(assetRevisionVersionHistoryAdapter);

            progressBar.setVisibility(View.VISIBLE);

            DBConn.getRequest(DBConn.getRecordURL("revisions?order=date_created,desc&filter=asset_revision_id,eq," + String.valueOf(assetModel.getAssetId())), this, new DBConn.ResponseCallback() {
                @Override
                public void innerResponse(Object object) {
                    try {
                        if (object instanceof JSONObject) {
                            System.out.println(((JSONObject) object).toString(4));
//                            projectItemViewProjectOwner.setText(((JSONObject) object).getString("full_name"));
                        }
                        else if (object instanceof JSONArray) {
                            System.out.println(((JSONArray) object).length());
                            progressBar.setVisibility(View.GONE);
                            for (int i = 0; i < ((JSONArray) object).length(); i++) {
                                try {
                                    JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);

                                    System.out.println(((JSONArray) object).toString(4));

                                    AssetVersionModel model = new AssetVersionModel(
                                            jsonObject.getString("revision_id"),
                                            jsonObject.getString("revision_id_file_path"),
                                            jsonObject.getInt("asset_revision_id"),
                                            jsonObject.getString("previous_revision_id"),
                                            jsonObject.isNull("date_created") ? null : LocalDateTime.parse(jsonObject.getString("date_created"), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                                            jsonObject.isNull("date_processed") ? null : LocalDateTime.parse(jsonObject.getString("date_processed"), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                                            jsonObject.getInt("revision_status"),
                                            jsonObject.getString("comment")
                                    );

                                    assetVersionModels.add(model);
                                }
                                catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }

                            assetRevisionRV.setAdapter(assetRevisionVersionHistoryAdapter);
                        }
                    } catch (Exception e) {
                        Toast.makeText(AssetRevisionActivity.this, "Unable to fetch version history data", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
            }, "Unable to connect to the database",
                "Unable to parse API response");

        }
    }
}