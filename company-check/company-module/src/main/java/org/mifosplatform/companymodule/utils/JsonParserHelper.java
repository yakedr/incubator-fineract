package org.mifosplatform.companymodule.utils;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.mifosplatform.companymodule.exceptions.InvalidJsonException;
import org.mifosplatform.companymodule.exceptions.DataValidationException;
import org.mifosplatform.companymodule.exceptions.UnsupportedParameterException;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Helper class to manipulate json.
 */
public class JsonParserHelper {

    public static JsonElement parse(final String json) {

        JsonElement parsedElement = null;
        if (StringUtils.isNotBlank(json)) {
            parsedElement = new JsonParser().parse(json);
        }
        return parsedElement;
    }

    public static boolean parameterExists(final String parameterName, final JsonElement element) {
        if (element == null) {
            return false;
        }
        return element.getAsJsonObject().has(parameterName);
    }

    public static void checkForUnsupportedParameters(final Type typeOfMap, final String json, final Set<String> supportedParams) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Map<String, Object> requestMap = new Gson().fromJson(json, typeOfMap);

        final List<String> unsupportedParameterList = new ArrayList<>();
        for (final String providedParameter : requestMap.keySet()) {
            if (!supportedParams.contains(providedParameter)) {
                unsupportedParameterList.add(providedParameter);
            }
        }

        if (!unsupportedParameterList.isEmpty()) {
            throw new UnsupportedParameterException(unsupportedParameterList);
        }
    }

    public static Long extractLongNamed(final String parameterName, final JsonElement element) {
        Long longValue = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String stringValue = primitive.getAsString();
                if (StringUtils.isNotBlank(stringValue)) {
                    longValue = Long.valueOf(stringValue);
                }
            }
        }
        return longValue;
    }

    public static String extractStringNamed(final String parameterName, final JsonElement element) {
        String stringValue = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String valueAsString = primitive.getAsString();
                if (StringUtils.isNotBlank(valueAsString)) {
                    stringValue = valueAsString;
                }
            }
        }
        return stringValue;
    }

    public static Integer extractIntegerWithLocaleNamed(final String parameterName, final JsonElement element) {
        Integer value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            final Locale locale = extractLocaleValue(object);
            value = extractIntegerNamed(parameterName, object, locale);
        }
        return value;
    }

    public static Integer extractIntegerNamed(final String parameterName, final JsonElement element, final Locale locale) {
        Integer value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String valueAsString = primitive.getAsString();
                if (StringUtils.isNotBlank(valueAsString)) {
                    value = convertToInteger(valueAsString, parameterName, locale);
                }
            }
        }
        return value;
    }

    public static String extractDateFormatParameter(final JsonObject element) {
        String value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            final String dateFormatParameter = "dateFormat";
            if (object.has(dateFormatParameter) && object.get(dateFormatParameter).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(dateFormatParameter).getAsJsonPrimitive();
                value = primitive.getAsString();
            }
        }
        return value;
    }

    public static Locale extractLocaleParameter(final JsonObject element) {
        Locale clientApplicationLocale = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            String locale = null;
            final String localeParameter = "locale";
            if (object.has(localeParameter) && object.get(localeParameter).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(localeParameter).getAsJsonPrimitive();
                locale = primitive.getAsString();
                clientApplicationLocale = localeFromString(locale);
            }
        }
        return clientApplicationLocale;
    }

    public static LocalDate extractLocalDateNamed(final String parameterName, final JsonElement element) {

        LocalDate value = null;

        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            final String dateFormat = extractDateFormatParameter(object);
            final Locale clientApplicationLocale = extractLocaleParameter(object);
            value = extractLocalDateNamed(parameterName, object, dateFormat, clientApplicationLocale);
        }
        return value;
    }

    public static LocalDate extractLocalDateNamed(final String parameterName, final JsonObject element, final String dateFormat,
                                                  final Locale clientApplicationLocale) {
        LocalDate value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String valueAsString = primitive.getAsString();
                if (StringUtils.isNotBlank(valueAsString)) {
                    value = convertFrom(valueAsString, parameterName, dateFormat, clientApplicationLocale);
                }
            }
        }
        return value;
    }

    private static Locale extractLocaleValue(final JsonObject object) {
        Locale clientApplicationLocale = null;
        String locale = null;
        if (object.has("locale") && object.get("locale").isJsonPrimitive()) {
            final JsonPrimitive primitive = object.get("locale").getAsJsonPrimitive();
            locale = primitive.getAsString();
            clientApplicationLocale = localeFromString(locale);
        }
        return clientApplicationLocale;
    }

    public static LocalDate convertFrom(final String dateAsString, final String parameterName, final String dateFormat,
                                        final Locale clientApplicationLocale) {

        return convertDateTimeFrom(dateAsString, parameterName,
                dateFormat, clientApplicationLocale).toLocalDate();
    }

    public static LocalDateTime convertDateTimeFrom(final String dateTimeAsString, final String parameterName,
                                                    final String dateTimeFormat, final Locale clientApplicationLocale) {

        validateDateFormatAndLocale(parameterName, dateTimeFormat, clientApplicationLocale);
        LocalDateTime eventLocalDateTime = null;
        if (StringUtils.isNotBlank(dateTimeAsString)) {
            try {
                eventLocalDateTime = DateTimeFormat.forPattern(dateTimeFormat).withLocale(clientApplicationLocale)
                        .parseLocalDateTime(dateTimeAsString.toLowerCase(clientApplicationLocale));
            } catch (final IllegalArgumentException e) {
                final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
                final ApiParameterError error = ApiParameterError.parameterError("validation.msg.invalid.dateFormat.format", "The parameter "
                        + parameterName + " is invalid based on the dateFormat: '" + dateTimeFormat + "' and locale: '"
                        + clientApplicationLocale + "' provided:", parameterName, eventLocalDateTime, dateTimeFormat);
                dataValidationErrors.add(error);

                throw new DataValidationException(dataValidationErrors);
            }
        }

        return eventLocalDateTime;
    }

    private static void validateDateFormatAndLocale(final String parameterName, final String dateFormat,
                                                    final Locale clientApplicationLocale) {
        if (StringUtils.isBlank(dateFormat) || clientApplicationLocale == null) {

            final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
            if (StringUtils.isBlank(dateFormat)) {
                final String defaultMessage = new StringBuilder("The parameter '" + parameterName
                        + "' requires a 'dateFormat' parameter to be passed with it.").toString();
                final ApiParameterError error = ApiParameterError.parameterError("validation.msg.missing.dateFormat.parameter",
                        defaultMessage, parameterName);
                dataValidationErrors.add(error);
            }
            if (clientApplicationLocale == null) {
                final String defaultMessage = new StringBuilder("The parameter '" + parameterName
                        + "' requires a 'locale' parameter to be passed with it.").toString();
                final ApiParameterError error = ApiParameterError.parameterError("validation.msg.missing.locale.parameter", defaultMessage,
                        parameterName);
                dataValidationErrors.add(error);
            }
            throw new DataValidationException(dataValidationErrors);
        }

    }

    public static Integer convertToInteger(final String numericalValueFormatted, final String parameterName, final Locale clientApplicationLocale) {

        if (clientApplicationLocale == null) {

            final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
            final String defaultMessage = new StringBuilder("The parameter '" + parameterName
                    + "' requires a 'locale' parameter to be passed with it.").toString();
            final ApiParameterError error = ApiParameterError.parameterError("validation.msg.missing.locale.parameter", defaultMessage,
                    parameterName);
            dataValidationErrors.add(error);

            throw new DataValidationException(dataValidationErrors);
        }

        try {
            Integer number = null;

            if (StringUtils.isNotBlank(numericalValueFormatted)) {

                String source = numericalValueFormatted.trim();

                final NumberFormat format = NumberFormat.getInstance(clientApplicationLocale);
                final DecimalFormat df = (DecimalFormat) format;
                final DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
                df.setParseBigDecimal(true);

                // http://bugs.sun.com/view_bug.do?bug_id=4510618
                final char groupingSeparator = symbols.getGroupingSeparator();
                if (groupingSeparator == '\u00a0') {
                    source = source.replaceAll(" ", Character.toString('\u00a0'));
                }

                final Number parsedNumber = df.parse(source);

                final double parsedNumberDouble = parsedNumber.doubleValue();
                final int parsedNumberInteger = parsedNumber.intValue();

                if (source.contains(Character.toString(symbols.getDecimalSeparator()))) {
                    throw new ParseException(source, 0);
                }

                if (!Double.valueOf(parsedNumberDouble).equals(Double.valueOf(Integer.valueOf(parsedNumberInteger)))) {
                    throw new ParseException(
                            source, 0);
                }

                number = parsedNumber.intValue();
            }

            return number;
        } catch (final ParseException e) {

            final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
            final ApiParameterError error = ApiParameterError.parameterError("validation.msg.invalid.integer.format", "The parameter "
                    + parameterName + " has value: " + numericalValueFormatted + " which is invalid integer value for provided locale of ["
                    + clientApplicationLocale.toString() + "].", parameterName, numericalValueFormatted, clientApplicationLocale);
            error.setValue(numericalValueFormatted);
            dataValidationErrors.add(error);

            throw new DataValidationException(dataValidationErrors);
        }
    }

    public static Locale localeFromString(final String localeAsString) {

        if (StringUtils.isBlank(localeAsString)) {
            final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
            final ApiParameterError error = ApiParameterError.parameterError("validation.msg.invalid.locale.format",
                    "The parameter locale is invalid. It cannot be blank.", "locale");
            dataValidationErrors.add(error);

            throw new DataValidationException(dataValidationErrors);
        }

        String languageCode = "";
        String countryCode = "";
        String variantCode = "";

        final String[] localeParts = localeAsString.split("_");

        if (localeParts != null && localeParts.length == 1) {
            languageCode = localeParts[0];
        }

        if (localeParts != null && localeParts.length == 2) {
            languageCode = localeParts[0];
            countryCode = localeParts[1];
        }

        if (localeParts != null && localeParts.length == 3) {
            languageCode = localeParts[0];
            countryCode = localeParts[1];
            variantCode = localeParts[2];
        }

        return localeFrom(languageCode, countryCode, variantCode);
    }

    private static Locale localeFrom(final String languageCode, final String courntryCode, final String variantCode) {

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();

        final List<String> allowedLanguages = Arrays.asList(Locale.getISOLanguages());
        if (!allowedLanguages.contains(languageCode.toLowerCase())) {
            final ApiParameterError error = ApiParameterError.parameterError("validation.msg.invalid.locale.format",
                    "The parameter locale has an invalid language value " + languageCode + " .", "locale", languageCode);
            dataValidationErrors.add(error);
        }

        if (StringUtils.isNotBlank(courntryCode.toUpperCase())) {
            final List<String> allowedCountries = Arrays.asList(Locale.getISOCountries());
            if (!allowedCountries.contains(courntryCode)) {
                final ApiParameterError error = ApiParameterError.parameterError("validation.msg.invalid.locale.format",
                        "The parameter locale has an invalid country value " + courntryCode + " .", "locale", courntryCode);
                dataValidationErrors.add(error);
            }
        }

        if (!dataValidationErrors.isEmpty()) {
            throw new DataValidationException(dataValidationErrors);
        }

        return new Locale(languageCode.toLowerCase(), courntryCode.toUpperCase(), variantCode);
    }

}