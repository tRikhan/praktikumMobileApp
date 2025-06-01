package com.example.karkuratoru;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    private TextView tvDisplay;
    private String currentInput = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isOperatorPressed = false;
    private boolean isResultDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setClickListeners();
    }
    
    private void initializeViews() {
        tvDisplay = findViewById(R.id.tvDisplay);
    }
    
    private void setClickListeners() {
        // Number buttons
        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);
        
        // Operator buttons
        findViewById(R.id.btnPlus).setOnClickListener(this);
        findViewById(R.id.btnMinus).setOnClickListener(this);
        findViewById(R.id.btnMultiply).setOnClickListener(this);
        findViewById(R.id.btnDivide).setOnClickListener(this);
        
        // Action buttons
        findViewById(R.id.btnEquals).setOnClickListener(this);
        findViewById(R.id.btnClear).setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        
        int id = view.getId();
        
        if (id == R.id.btn0 || id == R.id.btn1 || id == R.id.btn2 || 
            id == R.id.btn3 || id == R.id.btn4 || id == R.id.btn5 || 
            id == R.id.btn6 || id == R.id.btn7 || id == R.id.btn8 || 
            id == R.id.btn9) {
            handleNumberInput(buttonText);
        } else if (id == R.id.btnPlus || id == R.id.btnMinus || 
                   id == R.id.btnMultiply || id == R.id.btnDivide) {
            handleOperatorInput(buttonText);
        } else if (id == R.id.btnEquals) {
            handleEqualsInput();
        } else if (id == R.id.btnClear) {
            handleClearInput();
        }
    }
    
    private void handleNumberInput(String number) {
        if (isResultDisplayed) {
            currentInput = "";
            isResultDisplayed = false;
        }
        
        if (isOperatorPressed) {
            currentInput = "";
            isOperatorPressed = false;
        }
        
        if (currentInput.equals("0")) {
            currentInput = number;
        } else {
            currentInput += number;
        }
        
        if (!operator.isEmpty() && !isOperatorPressed) {
            updateDisplay(String.valueOf((long)firstNumber) + " " + operator + " " + currentInput);
        } else {
            updateDisplay(currentInput);
        }
    }
    
    private void handleOperatorInput(String op) {
        if (!currentInput.isEmpty()) {
            if (!operator.isEmpty() && !isOperatorPressed) {
                // Perform the previous operation
                handleEqualsInput();
            }
            
            firstNumber = Double.parseDouble(currentInput);
            operator = op;
            isOperatorPressed = true;
            isResultDisplayed = false;
            
            updateDisplay(currentInput + " " + op);
        }
    }
    
    private void handleEqualsInput() {
        if (!operator.isEmpty() && !currentInput.isEmpty() && !isOperatorPressed) {
            double secondNumber = Double.parseDouble(currentInput);
            double result = 0;
            
            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "×":
                    result = firstNumber * secondNumber;
                    break;
                case "÷":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        updateDisplay("Error");
                        return;
                    }
                    break;
            }
            
            // Format the result to remove unnecessary decimal places
            String resultString;
            if (result == (long) result) {
                resultString = String.valueOf((long) result);
            } else {
                resultString = String.valueOf(result);
            }
            
            updateDisplay(resultString);
            currentInput = resultString;
            operator = "";
            isResultDisplayed = true;
        }
    }
    
    private void handleClearInput() {
        currentInput = "";
        operator = "";
        firstNumber = 0;
        isOperatorPressed = false;
        isResultDisplayed = false;
        updateDisplay("0");
    }
    
    private void updateDisplay(String text) {
        tvDisplay.setText(text);
    }
}