package com.example.tiptime;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptime.databinding.ActivityMainBinding;

import java.text.NumberFormat;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // Binding object instance with access to the views in the activity_main.xml layout
    ActivityMainBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout XML file and return a binding object instance
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Set the content view of the Activity to be the root view of the layout
        setContentView(binding.getRoot());

        // Setup a click listener on the calculate button to calculate the tip
        binding.calculateButton.setOnClickListener(v-> calculateTip());

        // Set up a key listener on the EditText field to listen for "enter" button presses
        /*
         * Key listener for hiding the keyboard when the "Enter" button is tapped.
         */
        binding.costOfServiceEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER){
                InputMethodManager enter = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                enter.hideSoftInputFromWindow(v.getWindowToken(),0);
                return true;
            }
            return false;
        });
    }

    private void calculateTip() {
        // Get the decimal value from the cost of service EditText field
        // If the cost is null or 0, then display 0 tip and exit this function early.
        double cost = 0.0;
        String stringInTextField = Objects.requireNonNull(binding.costOfServiceEditText.getText()).toString();
        if (stringInTextField.equals("") || stringInTextField.equals("0")){
            displayTip(0.0);
        }else{
            cost = Double.parseDouble(stringInTextField) ;
        }



        // Get the tip percentage based on which radio button is selected
        double tipPercentage;
        int id = binding.tipOptions.getCheckedRadioButtonId();
        if (id == R.id.option_twenty_percent){
            tipPercentage = 0.20;
        }else if (id == R.id.option_eighteen_percent){
            tipPercentage = 0.18;
        }else{
            tipPercentage = 0.15;
        }

        // Calculate the tip
        double tip = tipPercentage * cost;

        // If the switch for rounding up the tip toggled on (isChecked is true), then round up the
        // tip. Otherwise do not change the tip value.
        boolean roundUp = binding.roundUpSwitch.isChecked();
        if (roundUp) {
            // Take the ceiling of the current tip, which rounds up to the next integer, and store
            // the new value in the tip variable.
            tip = Math.ceil(tip);
        }

        // Display the formatted tip value onscreen
        displayTip(tip);
    }

    /*
     * Format the tip amount according to the local currency and display it onscreen.
     * Example would be "Tip Amount: $10.00".
     * @param v
     */
    private void displayTip(double tip) {
        String formattedTip = NumberFormat.getCurrencyInstance().format(tip);
        binding.tipResult.setText(getString(R.string.tip_amount, formattedTip));
    }


}
