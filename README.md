# Corda Insurance Application - Example by Arun

This is a project written to understand writing Corda applications. It
is intended for learning purpose. It demonstrates simple insurance policy
purchase and claims, transaction in the distributed ledger Corda.

This project references [corda documentation](https://docs.corda.net) and
[bootcamp-corda](https://github.com/corda/bootcamp-cordapp). Thanks to the
contributors and developers, most part of the template project could be
reused.

## Set up

1. Download and install Oracle JDK 8 JVM (minimum supported version 8u131)
2. Download and install IntelliJ Community Edition
   (supported versions 2017.x and 2018.x)
3. Download the arun-insurance repository:

       git clone https://github.com/corda/arun-insurance
       
4. Open IntelliJ. From the splash screen, click `Import Project`, select the
   `arun-insurance` folder and click `Open`
5. Select `Import project from external model > Gradle > Next > Finish`
6. Click `File > Project Structure…` and select the Project SDK (Oracle JDK 8,
   8u131+)

    i. Add a new SDK if required by clicking `New…` and selecting the JDK’s
       folder

7. Open the `Project` view by clicking `View > Tool Windows > Project`

## Running our CorDapp

Run the application with the following steps:

* Build a test network of nodes by opening a terminal window at the root of
  your project and running the following command:

    * Windows:   `gradlew.bat deployNodesJava`
    * macOS:     `./gradlew deployNodesJava`

* Start the nodes by running the following command:

    * Windows:   `build\nodes\runnodes.bat`
    * macOS:     `build/nodes/runnodes`

* Open the nodes are started, go to the terminal of InsuranceA (not
  the notary!) and run the following command to issue $100 insurance to Anil:

    `flow start InsuranceIssueFlow insurer: Anil, amount: 100`

* To view the insurance policy, run the following command in their respective
  terminals:

    `run vaultQuery contractStateType: arun_insurance.InsuranceState`

## Updating for offline use

* Run the `gatherDependencies` Gradle task from the root of the project to 
  gather all the CorDapp's dependencies in `lib/dependencies`
* Update `gradle/wrapper/gradle-wrapper.properties` to point to a local Gradle 
  distribution (e.g. 
  `distributionUrl=gradle-4.4.1-all.zip`)
* In `build.gradle`, under both `repositories` blocks, comment out any 
  repositories other than `flatDir { ... }`
