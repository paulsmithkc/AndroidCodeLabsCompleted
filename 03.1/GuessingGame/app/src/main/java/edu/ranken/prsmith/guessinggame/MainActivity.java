package edu.ranken.prsmith.guessinggame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random rand;
    private int prevGuess = 0;
    private int guessCount = 0;
    private int secretNumber = 0;
    private EditText guessEdit;
    private TextView outcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        guessEdit = findViewById(R.id.guess_edit);
        outcomeText = findViewById(R.id.outcome_text);

        rand = new Random();
        onReset(null);
    }

    public void onGuess(View view) {
        String guessString = guessEdit.getText().toString();
        if (guessString.length() == 0) {
            outcomeText.setText(R.string.guess_empty_error);
        } else {
            int guess = Integer.parseInt(guessString);
            if (guess < 1 || guess > 10) {
                outcomeText.setText(R.string.guess_out_of_range);
            } else {
                if (guess != prevGuess) {
                    ++guessCount;
                    prevGuess = guess;
                }
                if (guess == secretNumber) {
                    outcomeText.setText(getString(R.string.winning_text, secretNumber, guessCount));
                } else if (guess > secretNumber) {
                    outcomeText.setText(R.string.guess_high_text);
                } else {
                    outcomeText.setText(R.string.guess_low_text);
                }
            }
        }
    }

    public void onReset(View view) {
        secretNumber = 1 + rand.nextInt(10);
        guessEdit.setText("");
        outcomeText.setText(getString(R.string.secret_number_text, secretNumber));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("prevGuess", prevGuess);
        bundle.putInt("guessCount", guessCount);
        bundle.putInt("secretNumber", secretNumber);
        bundle.putString("guess", guessEdit.getText().toString());
        bundle.putString("outcome", outcomeText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        prevGuess = bundle.getInt("prevGuess", 0);
        guessCount = bundle.getInt("guessCount", 0);
        secretNumber = bundle.getInt("secretNumber", 0);
        guessEdit.setText(bundle.getString("guess"));
        outcomeText.setText(bundle.getString("outcome"));
    }
}
