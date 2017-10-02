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
package org.apache.fineract.portfolio.loanaccount.data;

import org.apache.fineract.organisation.monetary.data.CurrencyData;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

import static org.apache.fineract.organisation.monetary.domain.MoneyHelper.applyScale;

/**
 * Immutable data object representing loan summary information.
 */
@SuppressWarnings("unused")
public class LoanSummaryData {

    private final CurrencyData currency;
    private BigDecimal principalDisbursed;
    private BigDecimal principalPaid;
    private BigDecimal principalWrittenOff;
    private BigDecimal principalOutstanding;
    private BigDecimal principalOverdue;
    private BigDecimal interestCharged;
    private BigDecimal interestPaid;
    private BigDecimal interestWaived;
    private BigDecimal interestWrittenOff;
    private BigDecimal interestOutstanding;
    private BigDecimal interestOverdue;
    private BigDecimal feeChargesCharged;
    private BigDecimal feeChargesDueAtDisbursementCharged;
    private BigDecimal feeChargesPaid;
    private BigDecimal feeChargesWaived;
    private BigDecimal feeChargesWrittenOff;
    private BigDecimal feeChargesOutstanding;
    private BigDecimal feeChargesOverdue;
    private BigDecimal penaltyChargesCharged;
    private BigDecimal penaltyChargesPaid;
    private BigDecimal penaltyChargesWaived;
    private BigDecimal penaltyChargesWrittenOff;
    private BigDecimal penaltyChargesOutstanding;
    private BigDecimal penaltyChargesOverdue;
    private BigDecimal totalExpectedRepayment;
    private BigDecimal totalRepayment;
    private BigDecimal totalExpectedCostOfLoan;
    private BigDecimal totalCostOfLoan;
    private BigDecimal totalWaived;
    private BigDecimal totalWrittenOff;
    private BigDecimal totalOutstanding;
    private BigDecimal totalOverdue;
    private final LocalDate overdueSinceDate;

    public LoanSummaryData(final CurrencyData currency, final BigDecimal principalDisbursed, final BigDecimal principalPaid,
            final BigDecimal principalWrittenOff, final BigDecimal principalOutstanding, final BigDecimal principalOverdue,
            final BigDecimal interestCharged, final BigDecimal interestPaid, final BigDecimal interestWaived,
            final BigDecimal interestWrittenOff, final BigDecimal interestOutstanding, final BigDecimal interestOverdue,
            final BigDecimal feeChargesCharged, final BigDecimal feeChargesDueAtDisbursementCharged, final BigDecimal feeChargesPaid,
            final BigDecimal feeChargesWaived, final BigDecimal feeChargesWrittenOff, final BigDecimal feeChargesOutstanding,
            final BigDecimal feeChargesOverdue, final BigDecimal penaltyChargesCharged, final BigDecimal penaltyChargesPaid,
            final BigDecimal penaltyChargesWaived, final BigDecimal penaltyChargesWrittenOff, final BigDecimal penaltyChargesOutstanding,
            final BigDecimal penaltyChargesOverdue, final BigDecimal totalExpectedRepayment, final BigDecimal totalRepayment,
            final BigDecimal totalExpectedCostOfLoan, final BigDecimal totalCostOfLoan, final BigDecimal totalWaived,
            final BigDecimal totalWrittenOff, final BigDecimal totalOutstanding, final BigDecimal totalOverdue,
            final LocalDate overdueSinceDate) {
        this.currency = currency;
        this.principalDisbursed = principalDisbursed;
        this.principalPaid = principalPaid;
        this.principalWrittenOff = principalWrittenOff;
        this.principalOutstanding = principalOutstanding;
        this.principalOverdue = principalOverdue;
        this.interestCharged = interestCharged;
        this.interestPaid = interestPaid;
        this.interestWaived = interestWaived;
        this.interestWrittenOff = interestWrittenOff;
        this.interestOutstanding = interestOutstanding;
        this.interestOverdue = interestOverdue;
        this.feeChargesCharged = feeChargesCharged;
        this.feeChargesDueAtDisbursementCharged = feeChargesDueAtDisbursementCharged;
        this.feeChargesPaid = feeChargesPaid;
        this.feeChargesWaived = feeChargesWaived;
        this.feeChargesWrittenOff = feeChargesWrittenOff;
        this.feeChargesOutstanding = feeChargesOutstanding;
        this.feeChargesOverdue = feeChargesOverdue;
        this.penaltyChargesCharged = penaltyChargesCharged;
        this.penaltyChargesPaid = penaltyChargesPaid;
        this.penaltyChargesWaived = penaltyChargesWaived;
        this.penaltyChargesWrittenOff = penaltyChargesWrittenOff;
        this.penaltyChargesOutstanding = penaltyChargesOutstanding;
        this.penaltyChargesOverdue = penaltyChargesOverdue;
        this.totalExpectedRepayment = totalExpectedRepayment;
        this.totalRepayment = totalRepayment;
        this.totalExpectedCostOfLoan = totalExpectedCostOfLoan;
        this.totalCostOfLoan = totalCostOfLoan;
        this.totalWaived = totalWaived;
        this.totalWrittenOff = totalWrittenOff;
        this.totalOutstanding = totalOutstanding;
        this.totalOverdue = totalOverdue;
        this.overdueSinceDate = overdueSinceDate;
    }

    public BigDecimal getTotalOutstanding() {
        return this.totalOutstanding;
    }
    
    public BigDecimal getTotalPaidFeeCharges() {
        return feeChargesPaid ;
    }

    public LoanSummaryData getScaledValues() {
        this.feeChargesCharged = applyScale(this.feeChargesCharged);
        this.feeChargesDueAtDisbursementCharged = applyScale(this.feeChargesDueAtDisbursementCharged);
        this.feeChargesOutstanding = applyScale(this.feeChargesOutstanding);
        this.feeChargesOverdue = applyScale(this.feeChargesOverdue);
        this.feeChargesPaid = applyScale(this.feeChargesPaid);
        this.feeChargesWaived = applyScale(this.feeChargesWaived);
        this.feeChargesWrittenOff = applyScale(this.feeChargesWrittenOff);

        this.interestCharged = applyScale(this.interestCharged);
        this.interestOutstanding = applyScale(this.interestOutstanding);
        this.interestOverdue = applyScale(this.interestOverdue);
        this.interestPaid = applyScale(this.interestPaid);
        this.interestWaived = applyScale(this.interestWaived);
        this.interestWrittenOff = applyScale(this.interestWrittenOff);

        this.penaltyChargesCharged = applyScale(this.penaltyChargesCharged);
        this.penaltyChargesOutstanding = applyScale(this.penaltyChargesOutstanding);
        this.penaltyChargesOverdue = applyScale(this.penaltyChargesOverdue);
        this.penaltyChargesPaid = applyScale(this.penaltyChargesPaid);
        this.penaltyChargesWaived = applyScale(this.penaltyChargesWaived);
        this.penaltyChargesWrittenOff = applyScale(this.penaltyChargesWrittenOff);

        this.principalDisbursed = applyScale(this.principalDisbursed);
        this.principalOutstanding = applyScale(this.principalOutstanding);
        this.principalOverdue = applyScale(this.principalOverdue);
        this.principalPaid = applyScale(this.principalPaid);
        this.principalWrittenOff = applyScale(this.principalWrittenOff);

        this.totalExpectedRepayment = applyScale(this.totalExpectedRepayment);
        this.totalRepayment = applyScale(this.totalRepayment);
        this.totalExpectedCostOfLoan = applyScale(this.totalExpectedCostOfLoan);
        this.totalCostOfLoan = applyScale(this.totalCostOfLoan);
        this.totalWaived = applyScale(this.totalWaived);
        this.totalWrittenOff = applyScale(this.totalWrittenOff);
        this.totalOutstanding = applyScale(this.totalOutstanding);
        this.totalOverdue = applyScale(this.totalOverdue);

        return this;
    }

}