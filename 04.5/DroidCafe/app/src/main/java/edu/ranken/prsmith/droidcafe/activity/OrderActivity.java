package edu.ranken.prsmith.droidcafe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ranken.prsmith.droidcafe.R;

public class OrderActivity extends AppCompatActivity {
    public static final String EXTRA_ITEMS = "items";

    private TextView orderText;
    private EditText nameEdit;
    private EditText addressEdit;
    private EditText phoneEdit;
    private EditText emailEdit;
    private EditText noteEdit;
    private EditText dateEdit;
    private EditText timeEdit;
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
        dateEdit = findViewById(R.id.delivery_date_edit);
        timeEdit = findViewById(R.id.delivery_time_edit);
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

        dateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onDeliveryDate(v);
                }
            }
        });
        timeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onDeliveryTime(v);
                }
            }
        });

        Intent intent = getIntent();
        String[] items = intent.getStringArrayExtra(EXTRA_ITEMS);
        if (items != null) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < items.length; ++i) {
                result.append(items[i]).append('\n');
            }
            orderText.setText(result);
        }
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

        receipt.append("\nClick OK to confirm the purchase, or Cancel to abort.");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Purchase");
        builder.setMessage(receipt);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(OrderActivity.this, "Confirmed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(OrderActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    public void onClickDeliveryMode(View view) {
        //RadioButton radio = (RadioButton)view;
        //Toast.makeText(this, radio.getText(), Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onDeliveryDate(View view) {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format));
        if (dateEdit.getText().length() > 0) {
            try {
                Date parsed = format.parse(dateEdit.getText().toString());
                date.setTime(parsed);
            } catch (ParseException ex) {}
        }

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, month);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateEdit.setText(format.format(date.getTime()));
                //dateEdit.setText(String.format("%04d-%02d-%02d", year, month+1, dayOfMonth));
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(
            this,
            0,
            listener,
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    public void onDeliveryTime(View view) {
        Calendar time = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.time_format));
        if (timeEdit.getText().length() > 0) {
            try {
                Date parsed = format.parse(timeEdit.getText().toString());
                time.set(Calendar.HOUR_OF_DAY, parsed.getHours());
                time.set(Calendar.MINUTE, parsed.getMinutes());
            } catch (ParseException ex) {}
        }

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                timeEdit.setText(format.format(time.getTime()));
//                if (hourOfDay >= 12) {
//                    if (hourOfDay == 12) hourOfDay = 24;
//                    timeEdit.setText(String.format("%02d:%02d PM", hourOfDay - 12, minute));
//                } else {
//                    if (hourOfDay == 0) hourOfDay = 12;
//                    timeEdit.setText(String.format("%02d:%02d AM", hourOfDay, minute));
//                }
            }
        };
        TimePickerDialog dialog = new TimePickerDialog(
          this,
          0,
          listener,
          time.get(Calendar.HOUR_OF_DAY),
          time.get(Calendar.MINUTE),
          false
        );
        dialog.show();
    }
}
