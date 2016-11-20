package ro.code4.votehack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import io.realm.Realm;
import ro.code4.votehack.fragment.BranchSelectionFragment;
import ro.code4.votehack.net.HttpCallback;
import ro.code4.votehack.net.HttpClient;
import ro.code4.votehack.net.model.Section;
import ro.code4.votehack.net.model.response.VersionResponse;

public class ToolbarActivity extends BaseActivity implements Navigator {
    private DrawerLayout drawerLayout;
    private View menuButton;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        menuButton = findViewById(R.id.toolbar_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);

        initNavigationDrawer();

        navigateTo(BranchSelectionFragment.newInstance(), false);

//        checkFormVersion();
        getForms();
    }

    private void initNavigationDrawer() {
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        findViewById(R.id.menu_forms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ToolbarActivity.this, "Forms coming soon", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.menu_questions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ToolbarActivity.this, "Questions coming soon", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.menu_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ToolbarActivity.this, "Guide coming soon", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.menu_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSupportCenter();
            }
        });
    }

    private void checkFormVersion() {

    }

    private void getFormVersion() {
        HttpClient.getInstance().getFormVersion(new HttpCallback<VersionResponse>(VersionResponse.class) {
            @Override
            public void onSuccess(VersionResponse response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    private void getForms() {
        HttpClient.getInstance().getForm("A", new HttpCallback<Section[]>(Section[].class) {
            @Override
            public void onSuccess(Section[] sections) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(Arrays.asList(sections));
                realm.commitTransaction();
                realm.close();
            }

            @Override
            public void onError() {

            }
        });
        HttpClient.getInstance().getForm("B", new HttpCallback<Section[]>(Section[].class) {
            @Override
            public void onSuccess(Section[] sections) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(Arrays.asList(sections));
                realm.commitTransaction();
                realm.close();
            }

            @Override
            public void onError() {

            }
        });
        HttpClient.getInstance().getForm("C", new HttpCallback<Section[]>(Section[].class) {
            @Override
            public void onSuccess(Section[] sections) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(Arrays.asList(sections));
                realm.commitTransaction();
                realm.close();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void callSupportCenter() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + App.SERVICE_CENTER_PHONE_NUMBER));
        startActivity(callIntent);
    }

    @Override
    public void navigateTo(BaseFragment fragment) {
        navigateTo(fragment, true);
    }

    @Override
    public void navigateTo(BaseFragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }

    @Override
    public void navigateBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void setTitle(String title) {
        toolbarTitle.setText(title);
    }
}
