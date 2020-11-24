###Request

'PUT /meals/:id'

	curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100008' \
	--header 'Content-Type: application/json' \
	--data-raw '
	{
        "dateTime": "2020-01-31T20:00:00",
        "description": "Obed",
        "calories": 510
	}'

###Response

HTTP/1.1 204 No Content
    Date: Tue, 24 Nov 2020 12:25:21 GMT
    Status: 204 No Content
    Connection: close
	
###Request

'POST /meals'
	
	curl --location --request POST 'http://localhost:8080/topjava/rest/meals' \
	--header 'Content-Type: application/json' \
	--data-raw '{
    "dateTime": "2022-01-31T20:00:00",
    "description": "Obed",
    "calories": 510
	}'

###Response

 HTTP/1.1 201 Created
    Date: Tue, 24 Nov 2020 12:33:04 GMT
    Status: 201 Created
    Connection: close
    Content-Type: application/json    

{
    "id": 100017,
    "dateTime": "2022-01-31T20:00:00",
    "description": "Obed",
    "calories": 510,
    "user": null
}

###Request

'GETALL /meals'

	curl --location --request GET 'http://localhost:8080/topjava/rest/meals' \
	--header 'Content-Type: application/json' \
	
###Response

 HTTP/1.1 200 OK
    Date: Tue, 24 Nov 2020 12:38:56 GMT
    Status: 200 OK
    Connection: close
	
 {
        "id": 100017,
        "dateTime": "2022-01-31T20:00:00",
        "description": "Obed",
        "calories": 510,
        "excess": false
    },
    {
        "id": 100013,
        "dateTime": "2021-01-31T20:00:00",
        "description": "Ужин1",
        "calories": 510,
        "excess": false
    },
    {
        "id": 100008,
        "dateTime": "2020-01-31T20:00:00",
        "description": "Obed",
        "calories": 510,
        "excess": true
    },
    {
        "id": 100007,
        "dateTime": "2020-01-31T13:00:00",
        "description": "Обед",
        "calories": 1000,
        "excess": true
    },
    {
        "id": 100006,
        "dateTime": "2020-01-31T10:00:00",
        "description": "Завтрак",
        "calories": 500,
        "excess": true
    },
    {
        "id": 100005,
        "dateTime": "2020-01-31T00:00:00",
        "description": "Еда на граничное значение",
        "calories": 100,
        "excess": true
    },
    {
        "id": 100004,
        "dateTime": "2020-01-30T20:00:00",
        "description": "Ужин",
        "calories": 500,
        "excess": false
    },
    {
        "id": 100003,
        "dateTime": "2020-01-30T13:00:00",
        "description": "Обед",
        "calories": 1000,
        "excess": false
    },
    {
        "id": 100002,
        "dateTime": "2020-01-30T10:00:00",
        "description": "Завтрак",
        "calories": 500,
        "excess": false
    }


###Request

'GET FILTERED /meals/filter?startDate=&endDate=&startTime=&endTime='

	curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=10:00:00&endTime=23:00:00'
	--header 'Content-Type: application/json' \
	
###Response	

 HTTP/1.1 200 OK
    Date: Tue, 24 Nov 2020 12:40:44 GMT
    Status: 200 OK
    Connection: close
	
	 {
        "id": 100004,
        "dateTime": "2020-01-30T20:00:00",
        "description": "Ужин",
        "calories": 500,
        "excess": false
    },
    {
        "id": 100003,
        "dateTime": "2020-01-30T13:00:00",
        "description": "Обед",
        "calories": 1000,
        "excess": false
    },
    {
        "id": 100002,
        "dateTime": "2020-01-30T10:00:00",
        "description": "Завтрак",
        "calories": 500,
        "excess": false
    }
	
###Request

'GET /meals/:id'
	
	curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100004' \
	--header 'Content-Type: application/json' \
	
###Response

HTTP/1.1 200 OK
    Date: Tue, 24 Nov 2020 12:46:47 GMT
    Status: 200 OK
    Connection: close
	
{
    "id": 100004,
    "dateTime": "2020-01-30T20:00:00",
    "description": "Ужин",
    "calories": 500,
    "user": null
}

###Request

'DELETE /meals/:id'
	
	curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100006' \
	--header 'Content-Type: application/json' \
	
###Response	

HTTP/1.1 204 No Content
    Date: Tue, 24 Nov 2020 12:45:19 GMT
    Status: 204 No Content
    Connection: close