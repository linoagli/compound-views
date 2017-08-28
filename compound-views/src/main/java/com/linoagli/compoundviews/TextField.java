/**
 * CompoundViews Library Project.
 * com.linoagli.compoundviews
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.compoundviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class TextField extends FrameLayout {
    private TextInputLayout inputLayout;
    private TextInputEditText editText;

    public TextField(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public TextField(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextField(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setHint(int resource) {
        inputLayout.setHint(getResources().getString(resource));
    }

    private void init(Context context, AttributeSet attributeSet) {
        View view = inflate(context, R.layout.compoundviews_text_field, this);

        inputLayout = view.findViewById(R.id.til_textField_inputLayout);
        editText = view.findViewById(R.id.tiet_textField_editText);

        TypedArray styleableAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.TextField, 0, 0);

        try {
            int lines = styleableAttributes.getInt(R.styleable.TextField_android_lines, 1);
            editText.setLines(lines);

            int gravity = styleableAttributes.getInt(R.styleable.TextField_android_gravity, Gravity.START | Gravity.CENTER_VERTICAL);
            editText.setGravity(gravity);

            int hintResource = styleableAttributes.getResourceId(R.styleable.TextField_android_hint, -1);
            if (hintResource != -1) setHint(hintResource);

            int inputType = styleableAttributes.getInt(R.styleable.TextField_android_inputType, InputType.TYPE_CLASS_TEXT);
            editText.setInputType(inputType);
        }
        finally {
            styleableAttributes.recycle();
        }
    }

    public void setText(CharSequence text) {
        editText.setText(text);
    }

    public CharSequence getText() {
        return editText.getText().toString();
    }
}