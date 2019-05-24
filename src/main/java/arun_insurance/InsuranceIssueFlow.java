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
import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import java.security.PublicKey;
import java.util.List;

/**
 * This is the flow for issuing a new insurance policy.
 * Insurer has to accept the policy from insurance company.
 */
@InitiatingFlow
@StartableByRPC
public class InsuranceIssueFlow extends FlowLogic<SignedTransaction> {
    private final Party insurer;
    private final int amount;

    public InsuranceIssueFlow(Party insurer, int amount) {
        this.insurer = insurer;
        this.amount = amount;
    }

    // checkpoint the progress of the flow to the observers
    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    // Flow logic, things to be performed when a new insurance is registered for
    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {

        // Network identities required
        // Pick a notary service, for audit purposes
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        // We get a reference to our own identity.
        Party issuer = getOurIdentity();

        // Create transaction components
        // Create a insurance, from insurance company to the insurer
        InsuranceState insuranceState = new InsuranceState(issuer, insurer, amount);
        // Who should sign the issue flow
        List<PublicKey> requiredSigners = ImmutableList.of(insuranceState.getIssuer().getOwningKey());

        // Build the transaction, a mapping from input state to output state
        TransactionBuilder transactionBuilder = new TransactionBuilder(notary)
                .addOutputState(insuranceState, InsuranceContract.ID)
                .addCommand(new InsuranceContract.Commands.Issue(), requiredSigners);
        transactionBuilder.verify(getServiceHub());
        SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(transactionBuilder);

        // We get the transaction notarised and recorded automatically by the platform.
        return subFlow(new FinalityFlow(signedTransaction));
    }
}