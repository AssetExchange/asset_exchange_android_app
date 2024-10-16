package com.example.assetexdemo1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileSharingOptionsActivity extends AppCompatActivity {
    private ImageButton backProfileSharingOptions;
    private RadioGroup profileSharingOptionsRadioGroup;
    private Button profileSharingOptionsShareButton;
    private LinearLayout profileSharingOptionsShareLinkLinearLayout;
    private TextView profileSharingOptionsShareLinkText;
    private ImageButton profileSharingOptionsShareLinkCopyButton;
    private ImageView profileSharingOptionsShareLinkQRView;

    ProgressBar progressBar;
    String shareLink = null;
    UserModel userModel;

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
            backProfileSharingOptions = findViewById(R.id.backProfileSharingOptions);
            profileSharingOptionsRadioGroup = findViewById(R.id.profileSharingOptionsRadioGroup);
            profileSharingOptionsShareButton = findViewById(R.id.profileSharingOptionsShareButton);
            profileSharingOptionsShareLinkLinearLayout = findViewById(R.id.profileSharingOptionsShareLinkLinearLayout);
            profileSharingOptionsShareLinkText = findViewById(R.id.profileSharingOptionsShareLinkText);
            profileSharingOptionsShareLinkCopyButton = findViewById(R.id.profileSharingOptionsShareLinkCopyButton);
            profileSharingOptionsShareLinkQRView = findViewById(R.id.profileSharingOptionsShareLinkQRView);

            profileSharingOptionsShareLinkText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shareLink != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareLink));
                        startActivity(intent);
                    }
                }
            });

            profileSharingOptionsShareLinkCopyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shareLink != null) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                        ClipData clip = ClipData.newPlainText("link", shareLink);
                        clipboard.setPrimaryClip(clip);

                        ToastMessage.getInstance(ProfileSharingOptionsActivity.this).showShortMessage("Link copied to clipboard");
                    }
                }
            });

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

            DBConn.getRequest(
                DBConn.getRecordURL("profile_shares?join=users,profile_share_assets&filter=user_id,eq," + String.valueOf(userModel.getUserId())),
                ProfileSharingOptionsActivity.this,
                new DBConn.ResponseCallback() {
                    @Override
                    public void innerResponse(Object object) {
                        if (object instanceof JSONArray) {
                            if (((JSONArray) object).length() == 0) {
                                System.out.println("No shares yet.");
                            }
                            else if (((JSONArray) object).length() == 1) {
                                try {
                                    JSONObject jsonObject = ((JSONArray) object).getJSONObject(0);

                                    shareLink = DBConn.connectionHost + "share/profile/" + jsonObject.getString("share_link");

                                    profileSharingOptionsShareLinkText.setText(shareLink);

                                    try {
                                        BitMatrix bitMatrix = new MultiFormatWriter().encode(shareLink, BarcodeFormat.QR_CODE, 300, 300);

                                        Bitmap qrCodeBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);

                                        for (int x = 0; x < 300; x++) {
                                            for (int y = 0; y < 300; y++) {
                                                qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0x00FFFFFF);
                                            }
                                        }
                                        profileSharingOptionsShareLinkQRView.setImageBitmap(qrCodeBitmap);
                                    } catch (WriterException e) {
                                        profileSharingOptionsShareLinkQRView.setVisibility(View.GONE);
                                        e.printStackTrace();
                                    }

                                    profileSharingOptionsShareLinkLinearLayout.setVisibility(View.VISIBLE);
                                }
                                catch (JSONException e) {
                                    profileSharingOptionsShareLinkQRView.setVisibility(View.GONE);
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                },
                "Unable to connect to the database",
                "Unable to parse API response"
            );
        }
    }
}