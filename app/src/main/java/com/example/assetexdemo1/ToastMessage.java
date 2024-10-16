package com.example.assetexdemo1;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastMessage {
    private Context context;
    private static ToastMessage instance;

    private ToastMessage(Context context) {
        this.context = context;
    }

    public synchronized static ToastMessage getInstance(Context context) {
        if (instance == null) {
            instance = new ToastMessage(context);
        }
        return instance;
    }

    public void showLongMessage(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        else {
            showLongCustomToast(message, "smile");
        }
    }

    public void showLongMessage(String message, String iconType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        else {
            showLongCustomToast(message, iconType);
        }
    }

    public void showShortMessage(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
        else {
            showShortCustomToast(message, "smile");
        }
    }

    public void showShortMessage(String message, String iconType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
        else {
            showShortCustomToast(message, iconType);
        }
    }

    private View getLayout(String message, String iconType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) ((Activity) context).findViewById(R.id.toastParent));

        ImageView icon = (ImageView) layout.findViewById(R.id.toastIcon);
        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.toastView);
        TextView textView = (TextView) layout.findViewById(R.id.toastTextView);
        textView.setText(message);

        if (iconType.equals("smile")) {
            icon.setImageResource(R.drawable.ic_smile);
            linearLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#65EF8E")));
            textView.setTextColor(Color.BLACK);
        }
        else if (iconType.equals("frown")) {
            icon.setImageResource(R.drawable.ic_frown);
            linearLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D52C26")));
            textView.setTextColor(Color.WHITE);
        }
        else if (iconType.equals("yellow")) {
            icon.setImageResource(R.drawable.ic_smile);
            linearLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC01F")));
            textView.setTextColor(Color.BLACK);
        }

        return layout;
    }

    public void showLongCustomToast(String message, String iconType) {
        Toast toast = new Toast(context);

        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 12);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(getLayout(message, iconType));
        toast.show();

    }

    public void showShortCustomToast(String message, String iconType) {
        Toast toast = new Toast(context);

        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 12);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(getLayout(message, iconType));

        toast.show();
    }

}