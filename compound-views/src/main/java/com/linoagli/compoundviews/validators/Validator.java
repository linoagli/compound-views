/**
 * Compound Views Library Project.
 * com.linoagli.compoundviews.validators
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.compoundviews.validators;

public abstract class Validator {
    public final String errorMessage;

    public Validator(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public abstract boolean validate(String text);

    public static boolean isTextEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
}