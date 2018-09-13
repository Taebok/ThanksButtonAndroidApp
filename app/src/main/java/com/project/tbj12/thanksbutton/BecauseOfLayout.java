package com.project.tbj12.thanksbutton;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

class BecauseOfLayout extends LinearLayout implements TextWatcher {
    private TextView becauseofTextSize;

    public BecauseOfLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    // EditText max length를 설정함. xml과 병행할 것을 추천
    // myEditText.setFilters(new InputFilter[] {
    //        new PasswordCharFilter(), new InputFilter.LengthFilter(20)
    //});
    // 입력 텍스트의 길이를 확인하고 길이를 TextView에 출력
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.requestFocus();

        becauseofTextSize = findViewById(R.id.becauseof_text_size);
        EditText becauseofInput = findViewById(R.id.becauseof_input);
        becauseofInput.addTextChangedListener(this);
        becauseofInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});

        Button thanksButton = findViewById(R.id.becauseof_thanks_button);
        thanksButton.setOnClickListener(new OnClickedThankButton());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        becauseofTextSize.setText(String.valueOf(s.length()));
    }

    // Click Event Listener
    class OnClickedThankButton implements OnClickListener {
        @Override
        public void onClick(View v) {
            // 감사꺼리 데이터를 객체로 저장

        }
    }
}
