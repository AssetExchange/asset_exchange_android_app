package com.example.assetexdemo1.adapter;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.assetexdemo1.AssetExchangeApp;
import com.example.assetexdemo1.model.AssetModel;
import com.example.assetexdemo1.activity.AssetRevisionActivity;
import com.example.assetexdemo1.DBConn;
import com.example.assetexdemo1.R;
import com.example.assetexdemo1.ToastMessage;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectListItemsAdapter extends RecyclerView.Adapter<ProjectListItemsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<AssetModel> assetModels;

    public ProjectListItemsAdapter(Context context, ArrayList<AssetModel> assetModels) {
        this.context = context;
        this.assetModels = assetModels;
    }

    @NonNull
    @Override
    public ProjectListItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_project_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListItemsAdapter.ViewHolder holder, int position) {
        AssetModel model = assetModels.get(position);

        String ext = model.getLatestRevisionFilePath().trim().toLowerCase();

        if (ext.endsWith(".png") || ext.endsWith(".jpg") || ext.endsWith(".jpeg") || ext.endsWith(".gif")) {
            holder.projectListItemImageButton.setVisibility(View.VISIBLE);
            Glide.with(context)
                .load(DBConn.getURL("filegator/repository/user/" + model.getLatestRevisionFilePath()))
                .apply(
                    new RequestOptions()
                    .placeholder(android.R.drawable.screen_background_dark)
                    .error(R.drawable.password_cross)
                )
                .centerCrop() // .fitCenter()
                .dontAnimate()
                .into(holder.projectListItemImageButton);
            holder.projectListItemCaption.setVisibility(View.GONE);
            holder.itemView.setClickable(true);
            holder.projectListItemImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            holder.projectListItemCaption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        System.out.println(model.getLatestRevisionFilePath());

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DBConn.getFileURL(model.getLatestRevisionFilePath())));

                        request.setDescription("Downloading asset file...");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                        DBConn.getRequest(
                                DBConn.getRecordURL("files?filter=file_id,eq," + model.getLatestRevisionFilePath().split("\\.")[0]),
                                context,
                                new DBConn.ResponseCallback() {
                                    @Override
                                    public void innerResponse(Object object) {
                                        if (object instanceof JSONArray) {
                                            if (((JSONArray) object).length() >= 1) {
                                                try {
                                                    String fileName = ((JSONArray) object).getJSONObject(0).getString("file_name") + "." + ((JSONArray) object).getJSONObject(0).getString("file_ext");
                                                    request.setTitle(fileName);
                                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                                                    // get download service and enqueue file
                                                    DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                                    manager.enqueue(request);
                                                }
                                                catch (JSONException e) {
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
            });
        }
        else {
            Glide.with(context).clear(holder.projectListItemImageButton);
//            holder.projectListItemImageButton.setVisibility(View.GONE);
            holder.projectListItemImageButton.setBackgroundResource(R.drawable.bottom_nav_menu_filetext_icon);
            holder.projectListItemImageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.projectListItemCaption.setText(model.getAssetTitle());
            holder.projectListItemCaption.setVisibility(View.VISIBLE);
        }

        holder.projectListItemImageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, AssetRevisionActivity.class);
                intent.putExtra("asset_model", model);
                context.startActivity(intent);
                return true;
            }
        });
        holder.projectListItemOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, holder.projectListItemOptionsButton);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.project_asset_item_options_menu);

                    SharedPreferences sharedPref = AssetExchangeApp.context.getSharedPreferences(context.getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

                    if (sharedPref.getString("role_id", "4").equals("2")) {
                        popup.getMenu().removeItem(R.id.project_asset_item_option_delete);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.project_asset_item_option_download) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    System.out.println(model.getLatestRevisionFilePath());

                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DBConn.getFileURL(model.getLatestRevisionFilePath())));

                                    request.setDescription("Downloading asset file");
                                    request.setTitle("Asset Exchange");
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                                    DBConn.getRequest(
                                            DBConn.getRecordURL("files?filter=file_id,eq," + model.getLatestRevisionFilePath().split("\\.")[0]),
                                            context,
                                            new DBConn.ResponseCallback() {
                                                @Override
                                                public void innerResponse(Object object) {
                                                    if (object instanceof JSONArray) {
                                                        if (((JSONArray) object).length() >= 1) {
                                                            try {
                                                                String fileName = ((JSONArray) object).getJSONObject(0).getString("file_name") + "." + ((JSONArray) object).getJSONObject(0).getString("file_ext");
                                                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                                                                // get download service and enqueue file
                                                                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                                                manager.enqueue(request);
                                                            }
                                                            catch (JSONException e) {
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
                                return true;
                            }
                            else if (item.getItemId() == R.id.project_asset_item_option_details) {
                                Intent intent = new Intent(context, AssetRevisionActivity.class);
                                intent.putExtra("asset_model", model);
                                context.startActivity(intent);
                                return true;
                            }
                            else if (item.getItemId() == R.id.project_asset_item_option_delete) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                builder.setTitle("Delete Confirmation")
                                    .setMessage("Are you sure you want to delete this asset?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("action", "delete_asset");
                                            params.put("asset_id", String.valueOf(model.getAssetId()));
                                            DBConn.postRequest(DBConn.getURL("delete_asset.php"), context, params, new DBConn.ResponseCallback() {
                                                    @Override
                                                    public void innerResponse(Object object) {
                                                        ToastMessage.getInstance(context).showLongMessage(object.toString(), "yellow");
                                                        // Toast.makeText(context, object.toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                },
                                                "Unable to connect to the database",
                                                "Unable to parse API response"
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

                                return true;
                            }
                            else  {
                                return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
    }

    @Override
    public int getItemCount() {
        return assetModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton projectListItemImageButton;
        private final TextView projectListItemCaption;
        private final ImageButton projectListItemOptionsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectListItemImageButton = itemView.findViewById(R.id.projectListItemImageButton);
            projectListItemOptionsButton = itemView.findViewById(R.id.projectListItemOptionsButton);
            projectListItemCaption = itemView.findViewById(R.id.projectListItemCaption);
        }
    }
}
