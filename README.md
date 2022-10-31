<!-- GETTING STARTED -->
## Getting Started

# Account Information Service

Account Information Services contains REST APIs related user, account , balances, cards , transactions and withdrawals


# Which are not implemented (out of scope): 
Authentication & Authorizations
Deployment


### Prerequisites to run the application

Configure application.yml 
The default application.yml uses H2 database 

### Installation

1. Install Java19 & Maven3.8.6
2. Clone the repo
   ```sh
   git clone https://github.com/aiven-recruitment/Java-20221021-ltadiko.git
   ```
3. mvn package
   ```sh
   * Run nl.rabo.accountinformation.AccountInformationApplication class as java application from IDE
    or
   * mvn compile exec:java -Dexec.mainClass=nl.rabo.accountinformation.AccountInformationApplication
    or
   * docker build --quiet --build-arg ENVIRONMENT=local --tag latest . docker run -d -p 8080:8080 latest 
      ```

### Test API using postman

* Import Postman collection (AISP_APIs_Postman_collection.json) in postman

### Things could be done 

* Create more POJOs to separate entity models in Presentation layer
* Improve Test coverage
* Implementing pipeline


<!-- CONTACT -->
## Contact

Your Name - Lakshmaiah Tatikonda  - tlaxman88@gmail.com

Project Link: https://github.com/aiven-recruitment/Java-20221021-ltadiko.git] (https://github.com/ltadiko/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


### API Swagger documentation

* Link to API documentation (http://localhost:8080/swagger-ui/index.html#/)

### DATABASE

* Application uses in-memory h2 store (http://localhost:8080//h2-console)


<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

Use this space to list resources you find helpful and would like to give credit to. I've included a few of my favorites to kick things off!

* [Aiven for Apache Kafka®](https://docs.aiven.io/docs/products/kafka.html)
* [PostgreSQL® welcome#](https://docs.aiven.io/docs/products/postgresql.html)

<p align="right">(<a href="#readme-top">back to top</a>)</p>
