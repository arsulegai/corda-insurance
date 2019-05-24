/* Copyright 2019 Intel Corporation
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
------------------------------------------------------------------------------*/

package arun_insurance;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The insurance issued by the insurance company.
 * This has information on highest amount of money a person
 * is insured, the insurance company name, insurer. There's
 * also information on the time period for which the insurance
 * is valid.
 */
public class InsuranceState implements ContractState {

    // Insurance is bought from an agent
    private final Party issuer;
    // Insurer information do not change
    private final Party insurer;
    // Insurance amount does not change
    private final int amount;
    // Claimed amount can change depending on transaction
    private int claimed;

    public InsuranceState(Party issuer, Party insurer, int amount) {
        this.issuer = issuer;
        this.insurer = insurer;
        this.amount = amount;
        this.claimed = 0;
    }

    public Party getIssuer() {
        return this.issuer;
    }

    public Party getInsurer() {
        return this.insurer;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getClaimed() {
        return this.claimed;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(issuer, insurer);
    }
}
