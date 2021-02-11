package edu.ranken.prsmith.droidcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends AppCompatActivity {
    public static final String EXTRA_ITEMS = "items";

    private TextView orderText;
    private EditText nameEdit;
    private EditText addressEdit;
    private EditText phoneEdit;
    private EditText emailEdit;
    private EditText noteEdit;
    private RadioGroup deliveryGroup;
    private RadioButton sameDayDelivery;
    private RadioButton nextDayDelivery;
    private RadioButton pickUpDelivery;
    private Spinner phoneSpinner;
    private Spinner emailSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderText = findViewById(R.id.order_text);
        nameEdit = findViewById(R.id.name_edit);
        addressEdit = findViewById(R.id.address_edit);
        phoneEdit = findViewById(R.id.phone_edit);
        emailEdit = findViewById(R.id.email_edit);
        noteEdit = findViewById(R.id.note_edit);
        deliveryGroup = findViewById(R.id.delivery_group);
        sameDayDelivery = findViewById(R.id.same_day_delivery);
        nextDayDelivery = findViewById(R.id.next_day_delivery);
        pickUpDelivery = findViewById(R.id.pick_up_delivery);
        phoneSpinner = findViewById(R.id.phone_spinner);
        emailSpinner = findViewById(R.id.email_spinner);

        deliveryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.same_day_delivery:
                        Toast.makeText(OrderActivity.this, "same day", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.next_day_delivery:
                        Toast.makeText(OrderActivity.this, "next day", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.pick_up_delivery:
                        Toast.makeText(OrderActivity.this, "pick up", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

//        deliveryGroup.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
//            Toast.makeText(this, "lambda", Toast.LENGTH_SHORT).show();
//        });

        TextView.OnEditorActionListener editListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Toast.makeText(OrderActivity.this, "next", Toast.LENGTH_SHORT).show();
                }
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Toast.makeText(OrderActivity.this, "done", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        };

        nameEdit.setOnEditorActionListener(editListener);
        addressEdit.setOnEditorActionListener(editListener);
        phoneEdit.setOnEditorActionListener(editListener);
        emailEdit.setOnEditorActionListener(editListener);
        noteEdit.setOnEditorActionListener(editListener);

        Intent intent = getIntent();
        String[] items = intent.getStringArrayExtra(EXTRA_ITEMS);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < items.length; ++i) {
            result.append(items[i]).append('\n');
        }
        orderText.setText(result);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onBuy(View view) {
        hideKeyboard();

        StringBuilder receipt = new StringBuilder();
        receipt.append(nameEdit.getText()).append('\n');
        receipt.append(addressEdit.getText()).append('\n');
        receipt.append(phoneEdit.getText()).append(" | ");
        receipt.append(phoneSpinner.getSelectedItem()).append('\n');
        receipt.append(emailEdit.getText()).append(" | ");
        receipt.append(emailSpinner.getSelectedItem()).append('\n');
        receipt.append(noteEdit.getText()).append('\n');

        if (sameDayDelivery.isChecked()) {
            receipt.append(sameDayDelivery.getText()).append('\n');
        }
        if (nextDayDelivery.isChecked()) {
            receipt.append(nextDayDelivery.getText()).append('\n');
        }
        if (pickUpDelivery.isChecked()) {
            receipt.append(pickUpDelivery.getText()).append('\n');
        }

        Toast.makeText(this, receipt, Toast.LENGTH_LONG).show();
    }

    public void onClickDeliveryMode(View view) {
        //RadioButton radio = (RadioButton)view;
        //Toast.makeText(this, radio.getText(), Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
