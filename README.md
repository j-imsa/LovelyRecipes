# Lovely Recipes

> This is a personal java standalone app that provides a restful web service about foods and recipes.

#### Platform:

- Ubuntu,
- **Java 8**,
- **Spring boot**,
- Maven,
- Intellij Idea
- **H2 database**

#### How to run:

1. Clone the project
   - be aware about branches (development and master)
2. Open and run it in your ide (Intellij Idea recommended)
3. Visit localhost:8090/doc-ui to see about endpoints

### Sample Requests/Response:

#### Create a new recipe request:

```console
curl --location 'localhost:8090/recipes' \
--header 'Content-Type: application/json' \
--data '{
    "title": "Ghormeh",
    "ingredients": ["A", "B", "C"],
    "instructions": "first boil a, than add b and c",
    "consumers": 2,
    "vegetarian": false
}'
```

#### Response:

```json
{
   "action": true,
   "message": "",
   "date": "2023-03-13T09:15:10.777+00:00",
   "result": {
      "publicId": "zb3VFRBweDfa2mvZMfLmdpnu8a12b95G",
      "title": "Ghormeh",
      "ingredients": [
         "A",
         "B",
         "C"
      ],
      "instructions": "first boil a, than add b and c",
      "consumers": 2,
      "vegetarian": false
   }
}
```


#### Get recipes request:

> Get Pageable (by page and size) (*optional)

```console
curl --location 'localhost:8090/recipes/v2?page=1&size=5'
```

> Get by filters:

   - consumers: for how many people?
   - vegetarian: is it good for vegetarians?
   - include and exclude: what we need to include or exclude from ingredients
   - instructions: what we need to be contained in instructions
   - MORE INFO: visit localhost:8090/doc-ui, objects section

```console
curl --location 'localhost:8090/recipes?consumers=11&vegetarian=true&include=include&exclude=exclude&instructions=instructions'
```

#### Response:

```json
{
   "action": true,
   "message": "",
   "date": "2023-03-13T09:33:45.475+00:00",
   "result": [
      {
         "publicId": "qOzI3h84am8dt41PYYVvzEyhKxONQx2M",
         "title": "Ghormeh",
         "ingredients": [
            "A",
            "B",
            "C"
         ],
         "instructions": "first boil a, than add b and c",
         "consumers": 2,
         "vegetarian": false
      },
      {
         "publicId": "OysvpHSUsPG3ir2q03JON4cMKhG4tzP6",
         "title": "Gheymeh",
         "ingredients": [
            "A",
            "F"
         ],
         "instructions": "first boil a, than add f",
         "consumers": 4,
         "vegetarian": true
      }
   ]
}
```


> **_NOTE:_**  Delete and Update (PUT, not PATCH) are also available
