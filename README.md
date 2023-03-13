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

