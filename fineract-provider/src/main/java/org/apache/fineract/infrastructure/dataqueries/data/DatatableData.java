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
package org.apache.fineract.infrastructure.dataqueries.data;

import java.util.List;

/**
 * Immutable data object representing datatable data.
 */
public class DatatableData {


    private final String applicationTableName;

    private final String registeredTableName;

    private final List<ResultsetColumnHeaderData> columnHeaderData;


    public static DatatableData create(final String applicationTableName, final String registeredTableName,
            final List<ResultsetColumnHeaderData> columnHeaderData) {
        return new DatatableData(applicationTableName, registeredTableName, columnHeaderData);
    }

    private DatatableData(final String applicationTableName, final String registeredTableName,
            final List<ResultsetColumnHeaderData> columnHeaderData) {
        this.applicationTableName = applicationTableName;
        this.registeredTableName = registeredTableName;
        this.columnHeaderData = columnHeaderData;

    }

    public String getApplicationTableName() {
        return applicationTableName;
    }

    public String getRegisteredTableName() {
        return registeredTableName;
    }

    public List<ResultsetColumnHeaderData> getColumnHeaderData() {
        return columnHeaderData;
    }
}