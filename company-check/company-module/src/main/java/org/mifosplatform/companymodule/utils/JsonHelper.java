package org.mifosplatform.companymodule.utils;

import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class JsonHelper {

    private String jsonCommand;
    private JsonElement parsedCommand;

    private JsonHelper(String jsonCommand) {
        this.jsonCommand = jsonCommand;
        this.parsedCommand = JsonParserHelper.parse(jsonCommand);
    }

    public JsonHelper() {
    }

    public static JsonHelper instance(String jsonCommand) {
        return new JsonHelper(jsonCommand);
    }

    public String dateFormat() {
        return stringValueOfParameterNamed("dateFormat");
    }

    public String locale() {
        return stringValueOfParameterNamed("locale");
    }

    public boolean isChangeInLongParameterNamed(final String parameterName, final Long existingValue) {
        boolean isChanged = false;
        if (JsonParserHelper.parameterExists(parameterName, parsedCommand)) {
            final Long workingValue = JsonParserHelper.extractLongNamed(parameterName, parsedCommand);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public boolean isChangeInStringParameterNamed(final String parameterName, final String existingValue) {
        boolean isChanged = false;
        if (JsonParserHelper.parameterExists(parameterName, parsedCommand)) {
            final String workingValue = JsonParserHelper.extractStringNamed(parameterName, parsedCommand);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public boolean isChangeInLocalDateParameterNamed(final String parameterName, final LocalDate existingValue) {
        boolean isChanged = false;
        if (JsonParserHelper.parameterExists(parameterName, parsedCommand)) {
            final LocalDate workingValue = localDateValueOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public boolean isChangeInIntegerParameterNamed(final String parameterName, final Integer existingValue) {
        boolean isChanged = false;
        if (JsonParserHelper.parameterExists(parameterName, parsedCommand)) {
            final Integer workingValue = integerValueOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public String stringValueOfParameterNamed(final String parameterName) {
        final String value = JsonParserHelper.extractStringNamed(parameterName, this.parsedCommand);
        return StringUtils.defaultIfEmpty(value, "");
    }

    public LocalDate localDateValueOfParameterNamed(final String parameterName) {
        return JsonParserHelper.extractLocalDateNamed(parameterName, this.parsedCommand);
    }

    public Integer integerValueOfParameterNamed(final String parameterName) {
        return JsonParserHelper.extractIntegerWithLocaleNamed(parameterName, this.parsedCommand);
    }

    public Long longValueOfParameterNamed(final String parameterName) {
        return JsonParserHelper.extractLongNamed(parameterName, this.parsedCommand);
    }

    private boolean differenceExists(final Number baseValue, final Number workingCopyValue) {
        boolean differenceExists = false;

        if (baseValue != null) {
            if (workingCopyValue != null) {
                differenceExists = !baseValue.equals(workingCopyValue);
            } else {
                differenceExists = true;
            }
        } else {
            differenceExists = workingCopyValue != null;
        }

        return differenceExists;
    }

    private boolean differenceExists(final String baseValue, final String workingCopyValue) {
        boolean differenceExists = false;

        if (StringUtils.isNotBlank(baseValue)) {
            differenceExists = !baseValue.equals(workingCopyValue);
        } else {
            differenceExists = StringUtils.isNotBlank(workingCopyValue);
        }

        return differenceExists;
    }

    private boolean differenceExists(final LocalDate baseValue, final LocalDate workingCopyValue) {
        boolean differenceExists = false;

        if (baseValue != null) {
            differenceExists = !baseValue.equals(workingCopyValue);
        } else {
            differenceExists = workingCopyValue != null;
        }

        return differenceExists;
    }

    public String getJsonCommand() {
        return jsonCommand;
    }

    public void setJsonCommand(String jsonCommand) {
        this.jsonCommand = jsonCommand;
    }

    public void setParsedCommand(JsonElement parsedCommand) {
        this.parsedCommand = parsedCommand;
    }
}
