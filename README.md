# Fetch Backend Assessment

Java Springboot Application<br>
Validations has been added for some of the fields. But, kindly use all the valid data for each field in the requests.
<br><br>

**Steps to Run Application**
<br>
1. Change directory to backend. `cd backend`
2. Build docker image. `docker build -t fetch-assessment:latest .`
3. Check if the build is created. `docker images`
4. Create container and run application. `docker run -d -p 8080:8080 fetch-assessment:latest`
5. Check if container is created and running. `docker ps`
6. Use the curl command to hit the endpoints

   a. `curl -X POST http://localhost:8080/receipts/process -H "Content-Type: application/json" -d '{"retailer": "M&M Corner Market", "purchaseDate": "2022-03-20", "purchaseTime": "14:33", "items": [{"shortDescription": "Gatorade", "price": "2.25"}, {"shortDescription": "Gatorade", "price": "2.25"},{"shortDescription": "Gatorade", "price": "2.25"},{"shortDescription": "Gatorade", "price": "2.25"}], "total": "9.00"}'`

   b. Use the id received in the above part, in the following command to get points for receipt. `curl -X GET http://localhost:8080/receipts/{id}/points`<br>

7. To stop application and remove the container and image <br>
   a. `docker stop <container-id>`<br>
   b. `docker rm <container-id>`<br>
   c. `docker rmi <image-name>`<br>

<br>
<br>
<br>



**SOME OTHER EXAMPLES**


   `curl -X POST http://localhost:8080/receipts/process -H "Content-Type: application/json" -d '{"retailer":"Target","purchaseDate":"2022-01-01","purchaseTime":"13:01","items":[{"shortDescription":"Mountain Dew 12PK","price":"6.49"},{"shortDescription":"Emils Cheese Pizza","price":"12.25"},{"shortDescription":"Knorr Creamy Chicken","price":"1.26"},{"shortDescription":"Doritos Nacho Cheese","price":"3.35"},{"shortDescription":"Klarbrunn 12-PK 12 FL OZ","price":"12.00"}],"total":"35.35"}'`

<br>

   `curl -X POST http://localhost:8080/receipts/process -H "Content-Type: application/json" -d '{"retailer":"Walmart","purchaseDate":"2016-11-15","purchaseTime":"14:00","items":[{"shortDescription":"Advil Extra Strong","price":"6.49"}],"total":"6.49"}'`

   
