/**
 * Compound Views Library Project.
 * com.linoagli.compoundviews.validators
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.compoundviews.validators;

import android.util.Patterns;

public class EmailValidator extends Validator {
    public EmailValidator() {
        super("This email is not valid.");
    }

    @Override
    public boolean validate(String text) {
        return !Validator.isTextEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }
}