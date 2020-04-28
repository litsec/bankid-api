![Logo](https://github.com/litsec/eidas-opensaml/blob/master/docs/img/litsec-small.png)

------

# bankid-api

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/se.litsec.bankid/bankid-rp-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/se.litsec.litsec/bankid-rp-api) 

Java/Spring implementation of the BankID Relying Party API

The Litsec **bank-api** repository is an open source Java implementation of the BankID Relying Party API.

### Contents

* bankid-rp-api - A library that contains Java classes that represents the BankID API and structures.
* bankid-demo - A Spring boot demo application for how to use the bankid-api library. This work has only started and will be updated to a functional application shortly.

### Maven

The **bankid-rp-api** jar is published to Maven central. Include the following in your POM file to get the dependency:

```
<dependency>
  <groupId>se.litsec.bankid</groupId>
  <artifactId>bankid-rp-api</artifactId>
  <version>${bankid-api.version}</version>
</dependency>
```

### Resources

* [Javadoc for the API](https://litsec.github.io/bankid-api/javadoc/)
* [BankID Technical Information](https://www.bankid.com/bankid-i-dina-tjanster/rp-info)
* [BankID Relying Party Guidelines v3.2.2](https://www.bankid.com/assets/bankid/rp/bankid-relying-party-guidelines-v3.2.2.pdf)

### Licenses

The) Litsec bankid-api project uses the following Open Source libraries:

* [Spring Framework](https://spring.io/projects/spring-framework) under the [Apache License](https://github.com/spring-projects/spring-framework/blob/master/src/docs/dist/license.txt).

* [ZXing](https://github.com/zxing/zxing) for QR-code generation under the [Apache License](https://github.com/zxing/zxing/blob/master/LICENSE).
	* The use of this library is optional, and you can ...

------

Copyright &copy; 2016-2020, [Litsec AB](http://www.litsec.se). Licensed under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).
![Logo](https://github.com/litsec/eidas-opensaml/blob/master/docs/img/litsec-small.png)
