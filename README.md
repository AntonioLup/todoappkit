# todoappkit

# endpoints tasks

Metodo Get tasks user
https://todoappkit.up.railway.app/api/{usuario}/todos
~~~
[
    {
        "taskId": 3,
        "title": "titulo 3",
        "category": null,
        "isCompleted": false,
        "createdOn": "2023-03-19",
        "tags": null
    }
]
~~~
Metodo Post
https://todoappkit.up.railway.app/api/{usuario}/todos
~~~
[
    {
        "title": "titulo 3"
    }
]
~~~
Metodo Put
https://todoappkit.up.railway.app/api/{usuario}/todos/{taskid}
~~~
[
    {
        "taskId": 3,
        "title": "titulo 4"
    }
]
~~~
Metodo Delete
https://todoappkit.up.railway.app/api/{usuario}/todos/{taskid}
~~~
[
    {
        "taskId": 3
    }
]
~~~

# endpoints user

Metodo Post
https://todoappkit.up.railway.app/api/auth/register
~~~
{
    "username": "",
    "email": "@gmail.com",
    "role": "USER",
    "password": ""
}
~~~

Metodo Post, refresh token
https://todoappkit.up.railway.app/api/auth/authenticate

