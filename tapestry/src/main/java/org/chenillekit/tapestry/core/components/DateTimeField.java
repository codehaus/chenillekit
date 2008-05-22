/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.json.JSONObject;

/**
 * A component used to collect a provided date/time from the user using a client-side JavaScript calendar. Non-JavaScript
 * clients can simply type into a text field.
 *
 * @author <a href="mailto:homburgs@googlemail.com">S.Homburg</a>
 * @version $Id$
 */
@IncludeStylesheet("datetimefield/datepicker.css")
@IncludeJavaScriptLibrary({"datetimefield/datepicker.js", "../prototype-base-extensions.js",
        "../prototype-date-extensions.js"})
public class DateTimeField extends AbstractField
{
    /**
     * The value parameter of a DateField must be a {@link java.util.Date}.
     */
    @Parameter(required = true, principal = true)
    private Date value;

    /**
     * The object that will perform input validation (which occurs after translation). The translate binding prefix is
     * generally used to provide this object in a declarative fashion.
     */
    @Parameter(defaultPrefix = "validate")
    @SuppressWarnings("unchecked")
    private FieldValidator<Object> validate = NOOP_VALIDATOR;

    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "datetimefield/calendar.png")
    private Asset icon;

    /**
     * the pattern describing the date and time format {@link java.text.SimpleDateFormat}.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "MM/dd/yyyy")
    private String datePattern;

    /**
     * a boolean value determining whether to display the time picker. Defaults to false.
     */
    @Parameter(defaultPrefix = BindingConstants.PROP, value = "false")
    private boolean timePicker;

    /**
     * a boolean value determining whether to display the time picker next to the date picker (true) or under it (false, default).
     */
    @Parameter(defaultPrefix = BindingConstants.PROP, value = "false")
    private boolean timePickerAdjacent;

    @Environmental
    private RenderSupport support;

    @Environmental
    private ValidationTracker tracker;

    @Inject
    private ComponentResources resources;

    @Inject
    private Messages messages;

    @Inject
    private Request request;

    @Inject
    private Locale locale;

    @Inject
    private FieldValidatorDefaultSource fieldValidatorDefaultSource;

    @Inject
    private FieldValidationSupport fieldValidationSupport;

    /**
     * For output, format nicely and unambiguously as four digits.
     */
    private DateFormat outputFormat;

    /**
     * When the user types a value, they may only type two digits for the year; SimpleDateFormat will do something
     * reasonable.  If they use the popup, it will be unambiguously 4 digits.
     */
    private DateFormat inputFormat;

    /**
     * The default value is a property of the container whose name matches the component's id. May return null if the
     * container does not have a matching property.
     */
    final Binding defaultValue()
    {
        return createDefaultParameterBinding("value");
    }

    /**
     * Computes a default value for the "validate" parameter using {@link org.apache.tapestry.services.FieldValidatorDefaultSource}.
     */
    final FieldValidator defaultValidate()
    {

        return fieldValidatorDefaultSource.createDefaultValidator(this, resources.getId(),
                                                                  resources.getContainerMessages(), locale,
                                                                  Date.class,
                                                                  resources.getAnnotationProvider("value"));
    }

    /**
     * Tapestry render phase method.
     * Initialize temporary instance variables here.
     */
    void setupRender()
    {
        outputFormat = new SimpleDateFormat(datePattern);
    }


    void beginRender(MarkupWriter writer)
    {
        String value = tracker.getInput(this);

        if (value == null) value = formatCurrentValue();

        String clientId = getClientId();

        writer.element("input",

                       "type", "text",

                       "class", "datepicker",

                       "name", getControlName(),

                       "id", clientId,

                       "value", value);

        writeDisabled(writer);

        validate.render(writer);

        resources.renderInformalParameters(writer);

        decorateInsideField();

        writer.end();

        // The setup parameters passed to Calendar.setup():

        JSONObject setup = new JSONObject();

        setup.put("icon", icon.toClientURL());
        setup.put("timePicker", timePicker);
        setup.put("timePickerAdjacent", timePickerAdjacent);
        setup.put("locale", request.getLocale().toString());
        if (timePicker)
            setup.put("dateTimeFormat", datePattern);
        else
            setup.put("dateFormat", datePattern);

        support.addScript("new Control.DatePicker('%s', %s);", getClientId(), setup);
    }

    private void writeDisabled(MarkupWriter writer)
    {
        if (isDisabled()) writer.attributes("disabled", "disabled");
    }


    private String formatCurrentValue()
    {
        if (value == null) return "";

        return outputFormat.format(value);
    }

    @Override
    protected void processSubmission(String elementName)
    {
        String value = request.getParameter(elementName);

        tracker.recordInput(this, value);

        Date parsedValue = null;

        try
        {
            if (InternalUtils.isNonBlank(value))
            {
                inputFormat = new SimpleDateFormat(datePattern);
                parsedValue = inputFormat.parse(value);
            }

        }
        catch (ParseException ex)
        {
            tracker.recordError(this, "Date value is not parseable.");
            return;
        }

        try
        {
            fieldValidationSupport.validate(parsedValue, resources, validate);

            this.value = parsedValue;
        }
        catch (ValidationException ex)
        {
            tracker.recordError(this, ex.getMessage());
        }
    }

    void injectResources(ComponentResources resources)
    {
        this.resources = resources;
    }

    void injectMessages(Messages messages)
    {
        this.messages = messages;
    }

    @Override
    public boolean isRequired()
    {
        return validate.isRequired();
    }
}