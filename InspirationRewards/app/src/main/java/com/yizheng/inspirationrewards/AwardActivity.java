package com.yizheng.inspirationrewards;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.yizheng.inspirationrewards.CommonInfo.makeCustomToast;

public class AwardActivity extends AppCompatActivity {

    private TextView nameTxt, pointsAwarded, dept, pos, story;
    private EditText pointsToSend, comment;
    private ImageView imageView;

    private Profile sender, receiver;

    private final static int MAX_LEN = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        Intent intent = getIntent();
        if (intent.hasExtra("sender")){
            sender = (Profile) intent.getSerializableExtra("sender");
        }
        if (intent.hasExtra("receiver")){
            receiver = (Profile) intent.getSerializableExtra("receiver");
        }
        nameTxt = findViewById(R.id.textView25);
        pointsAwarded = findViewById(R.id.textView27);
        dept = findViewById(R.id.textView29);
        pos = findViewById(R.id.textView31);
        story = findViewById(R.id.textView33);

        nameTxt.setText(receiver.getLastname()+", "+receiver.getFirstname());
        pointsAwarded.setText(new Integer(receiver.totalPointsAwarded()).toString());
        dept.setText(receiver.getDepartment());
        pos.setText(receiver.getPosition());
        story.setText(receiver.getStory());

        imageView = findViewById(R.id.imageView8);

        doConvert(receiver.getPhoto());

        pointsToSend = findViewById(R.id.editText10);
        comment = findViewById(R.id.editText9);

        setupTextView();
    }

    private void setupTextView() {
        EditText editText = findViewById(R.id.editText9);
        editText.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(MAX_LEN)
        });

        editText.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        int len = s.toString().length();
                        String countText = "Comment: (" + len + " of " + MAX_LEN + ")";
                        ((TextView) findViewById(R.id.textView35)).setText(countText);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                    }
                });
    }

    private void doConvert(String imgString) {

        byte[] imageBytes = Base64.decode(imgString, Base64.DEFAULT);
        //Log.d(TAG, "doConvert: Image byte array length: " + imgString.length());

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //Log.d(TAG, "doConvert: Bitmap created from Base 64 text");

        imageView.setImageBitmap(bitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String pointsString = pointsToSend.getText().toString();

        int points = Integer.valueOf(pointsString);

        if (!sender.hasEnough(points)){
            makeCustomToast(this, "Insufficient Points", Toast.LENGTH_LONG);
            return true;
        }

        String commentString = comment.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.logo);

        builder.setPositiveButton("OK", (dialog, id) -> {
            addRewards(points, commentString);
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> {
        });

        builder.setTitle("Save Changes?");

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    public void processResult(String s){
        if (s.startsWith("Error")) {
            makeCustomToast(this, s, Toast.LENGTH_LONG);
            return;
        }
        Intent intent = new Intent(this, LeaderboardActivity.class);
        setResult(RESULT_OK, intent);
        finish();
        return;
    }

    private void addRewards(int points, String commentString){

        new AwardAsyncTask(this, commentString, points).execute(sender, receiver);
    }
}
