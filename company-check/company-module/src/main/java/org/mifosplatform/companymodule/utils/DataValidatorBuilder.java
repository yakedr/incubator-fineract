/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mifosplatform.companymodule.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

public class DataValidatorBuilder {

    private final List<ApiParameterError> dataValidationErrors;
    private String resource;
    private String parameter;
    private String arrayPart;
    private Integer arrayIndex;
    private Object value;
    private boolean ignoreNullValue = false;

    public DataValidatorBuilder(final List<ApiParameterError> dataValidationErrors) {
        this.dataValidationErrors = dataValidationErrors;
    }

    public DataValidatorBuilder reset() {
        return new DataValidatorBuilder(this.dataValidationErrors).resource(this.resource);
    }

    public DataValidatorBuilder resource(final String resource) {
        this.resource = resource;
        return this;
    }

    public DataValidatorBuilder parameter(final String parameter) {
        this.parameter = parameter;
        return this;
    }

    public DataValidatorBuilder value(final Object value) {
        this.value = value;
        return this;
    }

    public DataValidatorBuilder notBlank() {
        if (this.value == null && this.ignoreNullValue) { return this; }

        if (this.value == null || StringUtils.isBlank(this.value.toString())) {
            String realParameterName = this.parameter;
            final StringBuilder validationErrorCode = new StringBuilder("validation.msg.").append(this.resource).append(".")
                    .append(this.parameter);
            if (this.arrayIndex != null && StringUtils.isNotBlank(this.arrayPart)) {
                validationErrorCode.append(".").append(this.arrayPart);
                realParameterName = new StringBuilder(this.parameter).append('[').append(this.arrayIndex).append("][")
                        .append(this.arrayPart).append(']').toString();
            }

            validationErrorCode.append(".cannot.be.blank");
            final StringBuilder defaultEnglishMessage = new StringBuilder("The parameter ").append(realParameterName).append(
                    " is mandatory.");
            final ApiParameterError error = ApiParameterError.parameterError(validationErrorCode.toString(),
                    defaultEnglishMessage.toString(), realParameterName, this.arrayIndex);
            this.dataValidationErrors.add(error);
        }
        return this;
    }

    public DataValidatorBuilder positiveAmount() {
        if (this.value == null && this.ignoreNullValue) { return this; }

        if (this.value != null) {
            final BigDecimal number = BigDecimal.valueOf(Double.valueOf(this.value.toString()));
            if (number.compareTo(BigDecimal.ZERO) <= 0) {
                final StringBuilder validationErrorCode = new StringBuilder("validation.msg.").append(this.resource).append(".")
                        .append(this.parameter).append(".not.greater.than.zero");
                final StringBuilder defaultEnglishMessage = new StringBuilder("The parameter ").append(this.parameter).append(
                        " must be greater than 0.");
                final ApiParameterError error = ApiParameterError.parameterError(validationErrorCode.toString(),
                        defaultEnglishMessage.toString(), this.parameter, number, 0);
                this.dataValidationErrors.add(error);
            }
        }
        return this;
    }
   
    public DataValidatorBuilder notExceedingLengthOf(final Integer maxLength) {
        if (this.value == null && this.ignoreNullValue) { return this; }

        if (this.value != null && this.value.toString().trim().length() > maxLength) {
            final StringBuilder validationErrorCode = new StringBuilder("validation.msg.").append(this.resource).append(".")
                    .append(this.parameter).append(".exceeds.max.length");
            final StringBuilder defaultEnglishMessage = new StringBuilder("The parameter ").append(this.parameter)
                    .append(" exceeds max length of ").append(maxLength).append(".");
            final ApiParameterError error = ApiParameterError.parameterError(validationErrorCode.toString(),
                    defaultEnglishMessage.toString(), this.parameter, maxLength, this.value.toString());
            this.dataValidationErrors.add(error);
        }
        return this;
    }

}