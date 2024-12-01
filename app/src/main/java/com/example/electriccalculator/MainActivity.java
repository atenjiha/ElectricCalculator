package com.example.electriccalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText unitsEditText, rebateEditText;
    private Button calculateButton, resetButton;
    private TextView resultTextView, totalChargesTextView;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        unitsEditText = findViewById(R.id.unitsEditText);
        rebateEditText = findViewById(R.id.rebateEditText);
        calculateButton = findViewById(R.id.calculateButton);
        resetButton = findViewById(R.id.resetButton);
        totalChargesTextView = findViewById(R.id.totalChargesTextView);
        resultTextView = findViewById(R.id.resultTextView);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.profile) {
                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                }
                else if (itemId == R.id.instruction) {
                    Intent instructionIntent = new Intent(MainActivity.this, InstructionActivity.class);
                    startActivity(instructionIntent);
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBill();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
    }

    private void handleNavigationItemClick(int itemId) {
        if (itemId == R.id.nav_profile) {
            showToast(getString(R.string.nav_profile) + " Clicked");
        } else if (itemId == R.id.nav_calculator) {
            showToast(getString(R.string.nav_calculator) + " Clicked");
        } else if (itemId == R.id.nav_instruction) {
            showToast("Instruction Clicked");
        }
        // Add more conditions for additional items if needed
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void calculateBill() {
        String number = unitsEditText.getText().toString().trim();
        String rebate = rebateEditText.getText().toString().trim();

        if (number.isEmpty() || rebate.isEmpty()) {
            showAlert("Error", "Please enter valid values.");
            return;
        }

        try {
            int unitUsed = Integer.parseInt(number);
            float rebatePercentage = Float.parseFloat(rebate);

            if (rebatePercentage < 0 || rebatePercentage > 5) {
                showAlert("Error", "Rebate must be between 0% to 5%");
                return;
            }

            float totalCharges;
            if (unitUsed <= 200) {
                totalCharges = unitUsed * 0.218f;
            } else if (unitUsed <= 300) {
                totalCharges = (200 * 0.218f) + ((unitUsed - 200) * 0.334f);
            } else if (unitUsed <= 600) {
                totalCharges = (200 * 0.218f) + (100 * 0.334f) + ((unitUsed - 300) * 0.516f);
            } else {
                totalCharges = (200 * 0.218f) + (100 * 0.334f) + (300 * 0.516f) + ((unitUsed - 600) * 0.546f);
            }

            float finalCharge = totalCharges - (totalCharges * (rebatePercentage / 100));

            totalChargesTextView.setText(String.format("RM %.2f", totalCharges));
            resultTextView.setText(String.format("RM %.2f", finalCharge));
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input format.");

        }
    }


    private void resetFields() {
        unitsEditText.setText("");
        rebateEditText.setText("");
        totalChargesTextView.setText("");
        resultTextView.setText("");
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}