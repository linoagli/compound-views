/**
 * Compound Views Library Project.
 * com.linoagli.compoundviews.validators
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.compoundviews.validators;

public class DefaultValidator extends Validator {
    public DefaultValidator() {
        super("This field cannot be empty.");
    }

    @Override
    public boolean validate(String text) {
        return !Validator.isTextEmpty(text);
    }
}