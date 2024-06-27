

function getUsers() {
    fetch("http://localhost:8080/admin/api")
        .then(function (response){
            return response.text();
        }).then(function (text){
        console.log(text);
    });
}

function getUser(id) {
    fetch("http://localhost:8080/admin/api/"+id)
        .then(function (response){
            return response.text();
        }).then(function (text){
        console.log(text);
    });
}

getUsers();
getUser(5);