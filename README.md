## S3-Storage-Service

## Tech Stack

* Java 17
* Spring Boot
* AWS S3
* Junit 5
* Docker
* TestContainers
* Rest-assured

## Local Development

1. start localstack in docker container
    ```
   docker run \
   --rm -it \
   -p 4566:4566 \
   -p 4510-4559:4510-4559 \
   localstack/localstack
    ```
2. [Optional] Install awslocal which is thin wrapper over aws cli for connecting to the localstack.

   ```
   sudo pip3 install awscli-local
   awslocal --version
   ```
3. Create a new bucket on awslocal using following command:
   ```
    awslocal s3api create-bucket --bucket rameshwars3bucket
    awslocal s3api list-buckets
   ```
   OR
   ```
   awslocal s3 mb s3://rameshwars3bucket1
   ```
   
4. List File in the s3 buckets with awslocal 
   ```
   awslocal s3 ls s3://rameshwars3bucket1
   ## OR
   awslocal s3 ls s3://rameshwars3bucket1/<directory>
   ```
   
5. Run application on local using
   ```
   ./mvnw boot:run
   ## OR
   ./mvnw boot:run -Dspring.profile.active=local
   ```
6. Run test on local
   ```
   ./mvnw test
   ```