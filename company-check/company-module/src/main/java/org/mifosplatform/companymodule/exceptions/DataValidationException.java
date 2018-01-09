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
package org.mifosplatform.companymodule.exceptions;

import org.mifosplatform.companymodule.utils.ApiParameterError;

import java.util.List;

/**
 * Exception thrown when problem with an API request.
 */
public class DataValidationException extends RuntimeException {

    private final String messageCode;
    private final String defaultUserMessage;
    private final List<ApiParameterError> errors;

    public DataValidationException(final List<ApiParameterError> errors) {
        this.messageCode = "validation.msg.validation.errors.exist";
        this.defaultUserMessage = "Validation errors exist.";
        this.errors = errors;
    }

    public DataValidationException(final String messageCode, final String defaultUserMessage,
                                   final List<ApiParameterError> errors) {
        this.messageCode = messageCode;
        this.defaultUserMessage = defaultUserMessage;
        this.errors = errors;
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public String getDefaultUserMessage() {
        return this.defaultUserMessage;
    }

    public List<ApiParameterError> getErrors() {
        return this.errors;
    }
}