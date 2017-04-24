package android.com.omdb.activity;

import android.com.omdb.R;
import android.com.omdb.helper.CustomTypeface;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Rahul D on 4/22/17.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private EditText mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWidgets();
    }

    private void getWidgets() {
        mSearchEditText = (EditText) findViewById(R.id.query_edit_text);
        mSearchEditText.setTypeface(CustomTypeface.getFontRegular(getBaseContext()));
        findViewById(R.id.search_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                String searchTerm = mSearchEditText.getText().toString();
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
                    Intent intent = new Intent(this, SearchResultActivity.class);
                    intent.putExtra(getString(R.string.search_term), searchTerm);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Please enter movie name", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
