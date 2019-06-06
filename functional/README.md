# AutoTestFramework - Functional
A customizable java automated testing framework for functionally testing web (including network traffic) &amp; api services along with integration to AWS services and different databases. Framework also supports specifying configuration key value pairs in a config.properties file (located in the resources folder). Secrets can be encrypted and stored in the config.properties file and will be decrypted in memory at runtime - details on how to set that up are below.

**Technologies used within the framework:**

* java
* maven
* testng
* selenium
* browser mob
* mongodb
* aws java sdk

## Setup
* Download & Install Java JDK
* Download & Configure [Maven] (https://maven.apache.org/download.cgi?Preferred=ftp://mirror.reverse.net/pub/apache/)

### Generate a keystore file
Run the following command
`keytool -genseckey -keystore .keystore -storetype jceks -storepass "{store_pass}" -keyalg AES -keysize 256 -alias configPasswords -keypass "{key_pass}"`

### Encryption & Decryption Keys
* Store the `.keystore` file in the user root of your system
* Create a file with the contents below called `.vault` and store it in the user root of your system

```
STORE_PASSWORD=
KEY_PASSWORD=
```
* Specify the `STORE_PASSWORD` and `KEY_PASSWORD` values in the file above with the values used when generating the keystore file

## Configure
Configure which group of tests are run and which environment to run the tests on in the `src/test/resources/testng.xml` file

### To utilize AWS Services

* Create a file with the contents below called `credentials` and store it in `~/.aws/credentials`

```
    # Move this credentials file to (~/.aws/credentials)
    # after you fill in your access and secret keys in the default profile
    # WARNING: To avoid accidental leakage of your credentials,
    #          DO NOT keep this file in your source directory.
    [default]
    aws_access_key_id=
    aws_secret_access_key=
    
    [devprofile]
    aws_access_key_id=
    aws_secret_access_key=
    
    [qaprofile]
    aws_access_key_id=
    aws_secret_access_key=
```
* Specify your `access key id` and `secret access key` values in the file above for the given environment, follow the pattern above for additional environments.

## Running Tests From Command Line
### To run tests with default parameters from command line run:
```
mvn clean test -Denv={environment} -Dgroups={scope}
```

### In order to have the tests point to a local instance of selenium server, run the following:
```
mvn clean test -Denv={environment} -Dgroups={scope} -Dselenium.grid={seleniumGrid_url}
```

### In order to have the tests point to a local instance of the application, run the following:
```
mvn clean test -Denv=LOCAL -Dgroups={scope} -Dapphost={local_app_url}
```

#### Installing a local instance Zalenium (selenium grid)
In order to set up a local instance of a selenium grid, you can either do so by installing a [selenium server](https://www.seleniumhq.org/docs/07_selenium_grid.jsp) or by installing [Zalenium](https://github.com/zalando/zalenium) on your local box - follow the instructions on the link.

## Viewing Detailed Results From a Run
Go to the following path
`
target/surefire-reports/emailable-report.html 
`
