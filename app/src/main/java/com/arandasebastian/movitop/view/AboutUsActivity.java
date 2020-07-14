package com.arandasebastian.movitop.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import com.arandasebastian.movitop.R;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        MaterialButton btnContactMe = findViewById(R.id.activity_about_us_materialbutton_contactme);
        MaterialButton btnBack = findViewById(R.id.activity_about_us_materialbutton_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnContactMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                String[] recipients={"sebastian.aranda.12@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL,recipients);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Contact from MoviTop App");
                emailIntent.setType("text/plain");
                PackageManager pm = getPackageManager();
                List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") ||
                            info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(emailIntent);
            }
        });
    }
}
