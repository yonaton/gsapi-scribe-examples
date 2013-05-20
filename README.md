To try this example, get the repo at https://github.com/jdevjram/gsapi-scribe-examples
 
Run "mvn clean package" on the downloaded code.
This will assemble a jar-file with the examples and the scribe dependency.

To run the example :

    /path/to/java -cp /path/to/gsapi-scribe-examples-1.0.0-jar-with-dependencies.jar \
      -Doauth.consumer.key=your-api-key \
      -Doauth.consumer.secret=your-api-secret \
      com.generalsentiment.thirdparty.api.NameOfExampleClass


