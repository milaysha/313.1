const userUrl = 'http://localhost:8080/api/users/current';


function getPage() {
    fetch(userUrl).then(response => response.json()).then(user =>
        getInformation(user))
}

function getInformation(user) {
    console.log(user)

    document.getElementById('basicTable').innerHTML = `<tr>
            <td>${user.id}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.username}</td>
            <td>${user.rolesToString}</td>

        </tr>`;

}

getPage();