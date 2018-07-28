package com.example.android.udacitypractice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final String LOGTAG = "MainActivity";
    int amount = 1;
    int unitPrice = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if (amount == 100) {
            Toast.makeText(this, R.string.more_than_100, Toast.LENGTH_SHORT).show();
            return;
        }
        displayQuantity(++amount);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (amount == 1){
            Toast.makeText(this, R.string.less_than_1, Toast.LENGTH_SHORT).show();
            return;
        }
        displayQuantity(--amount);
    }

    /**
     * This method is called when user check or uncheck the topping items
     */
    public void updateUnitPrice(View view) {
        CheckBox whippedCreamCheckbox = findViewById(R.id.toppings_item_checkbox_1);
        CheckBox chocolateCheckbox = findViewById(R.id.toppings_item_checkbox_2);

        String unitPriceWithCurrencyTag = NumberFormat.getCurrencyInstance().format(unitPrice);
        String whippedCreamPriceWithCurrencyTag;
        String chocolatePriceWithCurrencyTag;

        if (whippedCreamCheckbox.isChecked()){
            whippedCreamPriceWithCurrencyTag = " + " + NumberFormat.getCurrencyInstance().format(1);
        }
        else {
            whippedCreamPriceWithCurrencyTag = "";
        }

        if (chocolateCheckbox.isChecked()) {
            chocolatePriceWithCurrencyTag = " + " + NumberFormat.getCurrencyInstance().format(2);
        }
        else {
            chocolatePriceWithCurrencyTag = "";
        }

        TextView unitPriceTextView = findViewById(R.id.unit_price_text_view);
        String unitPriceText = unitPriceWithCurrencyTag + whippedCreamPriceWithCurrencyTag + chocolatePriceWithCurrencyTag;
        unitPriceTextView.setText(unitPriceText);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText nameInput = findViewById(R.id.name_text_input);
        String name = nameInput.getText().toString();
        if (name.equals("")){
            Toast.makeText(this,"Please type your name!", Toast.LENGTH_SHORT).show();
            return;
        }

        CheckBox whippedCreamCheckBox = findViewById(R.id.toppings_item_checkbox_1);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = findViewById(R.id.toppings_item_checkbox_2);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        String summary = createOrderSummary(name, hasWhippedCream, hasChocolate);

        String[] address = {name + "@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee order for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, summary);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculate the price of the order based on the current quantity, and return the total price.
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate is whether or not the user wants chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int whippedCreamPrice = hasWhippedCream ? 1 : 0;
        int chocolatePrice = hasChocolate ? 2 : 0;
        return amount * (unitPrice + whippedCreamPrice + chocolatePrice);
    }

    /**
     * Create a summary of the order.
     *
     * @param name the order name
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate is whether or not the user wants chocolate topping
     * @return summary message
     */
    @SuppressLint("StringFormatInvalid")
    private String createOrderSummary(String name, boolean hasWhippedCream, boolean hasChocolate) {
        String priceWithCurrencyTag = NumberFormat.getCurrencyInstance().format(calculatePrice(hasWhippedCream, hasChocolate));

        String whippedCream, chocolate;
        if (hasWhippedCream) {
            whippedCream = getString(R.string.boolean_true);
        }else {
            whippedCream = getString(R.string.boolean_false);
        }

        if (hasChocolate) {
            chocolate = getString(R.string.boolean_true);
        }else {
            chocolate = getString(R.string.boolean_false);
        }

        return getString(R.string.order_summary_name, name)
                + "\n" + getString(R.string.order_summary_whipped_cream) + whippedCream
                + "\n" + getString(R.string.order_summary_chocolate) + chocolate
                + "\n" + getString(R.string.order_summary_amount, amount)
                + "\n" + getString(R.string.order_summary_total_price, priceWithCurrencyTag)
                + "\n" + getString(R.string.thank_you);
    }

    /**
     * Display the given quantity value on the screen.
     *
     * @param amount of coffees
     */
    private void displayQuantity(int amount) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(""+amount);
    }

}
