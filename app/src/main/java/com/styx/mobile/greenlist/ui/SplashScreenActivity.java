package com.styx.mobile.greenlist.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.styx.mobile.greenlist.R;
import com.styx.mobile.greenlist.utils.Utils;

/**
 *
 */
public class SplashScreenActivity extends AppCompatActivity {
    void startMainActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String appPasskey = sharedPreferences.getString(getString(R.string.saved_password), Utils.PARAMETER_STRING_EMPTY);
        if (!appPasskey.equals(Utils.PARAMETER_STRING_EMPTY)) {
            LayoutInflater layoutInflater = LayoutInflater.from(SplashScreenActivity.this);
            View dialogueView = layoutInflater.inflate(R.layout.layout_dialogue_category, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
            final EditText userInput = (EditText) dialogueView.findViewById(R.id.dialogUserInput);
            userInput.setHint("Passkey");
            builder.setView(dialogueView).setTitle("Enter App Password").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    }

            );
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(userInput.getText()) && userInput.getText().toString().equals(appPasskey)) {
                                alertDialog.dismiss();
                                startMainActivity();
                            } else {
                                userInput.setError("Incorrect");
                            }

                        }
                    });
                }
            });
            alertDialog.show();
        } else {
            startMainActivity();
        }
    }
}
