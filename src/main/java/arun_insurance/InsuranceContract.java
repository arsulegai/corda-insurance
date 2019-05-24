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
import java_examples.ArtContract;
import java_examples.ArtState;
import net.corda.core.contracts.*;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

/**
 * InsuranceContract is the smart contract for the insurance related
 * activities.
 */
public class InsuranceContract implements Contract {
    public static String ID = "arun_insurance.InsuranceContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        CommandWithParties<InsuranceContract.Commands> command = requireSingleCommand(tx.getCommands(), InsuranceContract.Commands.class);

        if (command.getValue() instanceof InsuranceContract.Commands.Issue) {
            // Issue transaction rules...
            if (tx.getInputStates().size() != 0) throw new IllegalArgumentException("Insurance company cannot initiate a insurance with initial state");
            if (tx.getOutputStates().size() != 1) throw new IllegalArgumentException("Insurance company can only issue a new transaction");
            InsuranceState insuranceState = tx.outputsOfType(InsuranceState.class).get(0);

            if (insuranceState.getAmount() <= 0) {
                throw new IllegalArgumentException("Minimum amount to insure is $1");
            }

            final List<PublicKey> requiredSigners = ImmutableList.of(insuranceState.getIssuer().getOwningKey());
            if (!(tx.getCommands().get(0).getValue() instanceof Commands.Issue)) {
                throw new IllegalArgumentException("Supported operations are insurance issuance");
            } else if (!requiredSigners.get(0).equals(tx.getCommand(0).getSigners().get(0))) {
                throw new IllegalArgumentException("Transaction not signed by the insurance company");
            }
        } else throw new IllegalArgumentException("Unrecognised command.");

    }

    // Commands allowed to be performed on the InsuranceState
    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}