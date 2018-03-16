package com.alex.appa.utils;

import android.util.Patterns;
import android.widget.AutoCompleteTextView;

/**
 * Created by alex on 16.03.18.
 */

public class Validator implements AutoCompleteTextView.Validator {

    @Override
    public boolean isValid(CharSequence text) {



        return Patterns.WEB_URL.matcher(text).matches();
    }

    @Override
    public CharSequence fixText(CharSequence invalidText) {
        return null;
    }
}
