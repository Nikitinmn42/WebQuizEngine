# WebQuizEngine


### Description
This is a RESTful multi-users web service for creating and solving quizzes, built with Spring Boot and H2 database.  
To perform any operations with quizzes (create, solve, get one, get all, delete), the user has to be registered and
then authorized via HTTP Basic Auth by sending their email and password for each request.


### How to use
Requires Java 11 or greater.  
To start application run command:  
`./gradlew bootRun`


#### To perform operations with quizzes service provides next endpoints:
_Without authorization:_  
**Send POST request to register new user.**  
`/api/register`  
Request body should contain JSON object with email and password:  
```json
{  
   "email": "test@gmail.com",
   "password": "secret"
}
```
Email should be in a valid format and password should contain at least 5 characters.  
The service returns 200 (OK) status code if the registration has been completed successfully.  
If password or email not corresponding to restrictions or if email is already taken, the service will return the 
400 (Bad request) status code.  

_With authorization:_  
Authorization is performed by sending basic auth with each request.
For each request without authorization or with bad credentials service responds with code 401 (Unauthorized).
 

**Send POST request to add a new quiz.**  
`/api/quizzes`  
Request body should contain JSON object with a quiz:
```json
{
  "title": "The Java Logo",
  "text": "What is depicted on the Java logo?",
  "options": ["Robot","Tea leaf","Cup of coffee","Text 'Java'"],
  "answer": [2, 3]
}
```
where:  
- `title` is string contains title of quiz, should not be empty or null.  
- `text` is string contains question of the quiz, should not be empty or null.
- `options` is an array of strings, which contains possible answers to question.
- `answer` is an array of integers, which contains indexes of correct answers from options array. 
Can be empty if there's no correct answer in options.  
If quiz successfully added service returns 200 (OK) status code and the same quiz object with ID and without the answer.
```json
{
    "id": 9,
    "title": "The Java Logo",
    "text": "What is depicted on the Java logo?",
    "options": [
        "Robot",
        "Tea leaf",
        "Cup of coffee",
        "Text 'Java'"
    ]
}
```


**Send GET request with parameter `page` to request all existing quizzes.**  
`/api/quizzes?page=0`  
Service returns one page from all quizzes stored in DB.  
Each page contains 10 quizzes at most. 
<details>
  <summary>Click to see example.</summary>

```json
{
    "content": [
        {
            "id": 3,
            "title": "The Java Logo",
            "text": "What is depicted on the Java logo?",
            "options": [
                "Robot",
                "Tea leaf",
                "Cup of coffee",
                "Text 'Java'"
            ]
        },
        {
            "id": 4,
            "title": "The Java Logo",
            "text": "What is depicted on the Java logo?",
            "options": [
                "Robot",
                "Tea leaf",
                "Cup of coffee",
                "Text 'Java'"
            ]
        },
        {
            "id": 5,
            "title": "The Java Logo",
            "text": "What is depicted on the Java logo?",
            "options": [
                "Robot",
                "Tea leaf",
                "Cup of coffee",
                "Text 'Java'"
            ]
        }
    ],
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "pageNumber": 0,
        "pageSize": 10,
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 3,
    "first": true,
    "number": 0,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
    "numberOfElements": 3,
    "size": 10,
    "empty": false
}
```
</details>


**Send GET request to retrieve quiz by its ID.**  
`/api/quizzes/{id}`  
where `{id}` is integer corresponding to quizzes ID.  
Service returns quiz from DB if present or response "404 Not Found" if not.
```json
{
    "id": 9,
    "title": "The Java Logo",
    "text": "What is depicted on the Java logo?",
    "options": [
        "Robot",
        "Tea leaf",
        "Cup of coffee",
        "Text 'Java'"
    ]
}
```


**Send DELETE request to remove quiz by its ID.**  
`/api/quizzes/{id}`  
where `{id}` is integer corresponding to quizzes ID.  
Remove quiz can only user who created it.  
If the operation was successful, the service returns the 204 (No content) status code without any content.  
If the specified quiz does not exist, the server returns 404 (Not found). If the specified user is not the author of 
this quiz, the response is the 403 (Forbidden) status code.  


**Send POST request to solve the quiz.**  
`/api/quizzes/{id}/solve`  
where `{id}` is integer corresponding to quizzes ID.  
Request body should contain JSON object with the answer for a quiz:
```json
{
    "answer": [2, 3]
}
```
where `answer` is an array of integers, which contains indexes of correct answers from options array. 
Can be empty if there's no correct answer in options.  
Service returns a JSON with two fields: success (true or false) and string feedback:
```json
{
    "success": true,
    "feedback": "Congratulations, you solved the quiz correctly!"
}
```
or
```json
{
    "success": false,
    "feedback": "Sorry, your answer is wrong - try again!"
}
```
If the specified quiz does not exist, the server returns the 404 (Not found) status code.


**Send GET request with parameter `page` to request all successful quiz completions.**  
`/api/quizzes/completed?page=0`  
Service returns one page from all quiz completions of current user stored in DB corresponding to page number.  
Records sorted by time of completion starting from the most recent.
<details>
  <summary>Click to see example.</summary>

```json
{
    "content": [
        {
            "completedAt": "2021-02-19T17:05:58.855884",
            "id": 14
        },
        {
            "completedAt": "2021-02-19T17:05:41.308778",
            "id": 11
        },
        {
            "completedAt": "2021-02-19T17:05:36.545765",
            "id": 10
        },
        {
            "completedAt": "2021-02-19T17:04:53.130375",
            "id": 5
        }
    ],
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "pageNumber": 0,
        "pageSize": 10,
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 4,
    "first": true,
    "number": 0,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
    "numberOfElements": 4,
    "size": 10,
    "empty": false
}
```
</details>
