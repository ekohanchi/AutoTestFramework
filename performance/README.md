# AutoTestFramework - Performance
A customizable performance testing automation framework, utilizing jmeter and jmeter-maven plugin at its core to execute a configurable load test from the command line. Using a few other technologies, this framework will also allow you to capture preview results and trends over time. This will also allow the user to run perforance and load test tests as part of a CI process as needed.

A sample jmeter script has been created to demonstrate how it all works together, and how it can be run from the command line. The jmeter script does a load test and compares the results of a few different search engines.

The details within the setup steps go through the steps of setting up external services such as elastic search and grafana locally, but one can certainly setup these services on and external instance such as an AWS instance. The specific versions that were used for this setup are specified below, other version may also work but they were not tested as part of this.

**Technologies used within the framework:**

* java - v1.8
* maven - v3.5.4
* jmeter - v4.0
* jmeter-maven plugin - v2.9
* elasticsearch - v6.7.1
* grafana - v6.0.2

## Setup
### Services & Applications
* Download & Install Java JDK
* Download & Configure [Maven] (https://maven.apache.org/download.cgi?Preferred=ftp://mirror.reverse.net/pub/apache/)
* Download & Start [Jmeter] (https://jmeter.apache.org/download_jmeter.cgi)
* Download [Jmeter Plugins Manager](https://jmeter-plugins.org/install/Install/)
* Download & Start [ElasticSearch] (https://www.elastic.co/downloads/elasticsearch)
* Download & Start [Grafana](https://grafana.com/grafana/download)

### Configuration
* Follow the steps [here] (https://dzone.com/articles/jmeter-elasticsearch-live-monitoring) to quickly setup and configure your instance of grafana to communicate with elasticsearch by adding a data source
* For the value of the `Index name` specifiy it to be `[tech-]YYYY.MM.DD` (this can be changed in the future by modifying the index name in the pom.xml)
* Set the `Pattern` to be `Daily`
* Set the `Max concurrent Shard Requests` value to be `256`
* When importing a grafana dashboard the following can be used - [grafanaJmeterGenericDashboard.json](setup/grafanaJmeterGenericDashboard.json)

**Optional**

* The pom.xml file has been configured with properties values and such to work with the respective sample jmeter file created, however it can be configured further as needed - refer to the [Jmeter-Maven Plugin] (https://github.com/jmeter-maven-plugin/jmeter-maven-plugin) documentation for more details
* Configuring the jmeter elasticsearch backend listener should be pretty straight forward - if needed more details can be found [here] (https://github.com/delirius325/jmeter-elasticsearch-backend-listener)

## Usage:
Open the GUI & set the number of threads or users you wish to test and execute. Ensure that the GUI is only used for debugging and creating/modifing the script but it is not used for the actual load/performance testing. Load tests should be done by running the command line. Refer to the [Jmeter Documentation] (https://jmeter.apache.org/usermanual/get-started.html) for more details.

### Running from command line
`mvn clean verify -Denv={env} -Dmain.threads={threads} -Dmain.loop={loop}`

i.e.
`mvn clean verify -Denv=prod -Dmain.threads=5 -Dmain.loop=1`