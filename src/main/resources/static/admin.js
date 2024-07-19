const url = 'http://localhost:8080/api';

let userRoles = [];

function getAllUsers() {
    fetch(`${url}/users/`)
        .then(res => res.json())
        .then(data => {
            loadTable(data)
        })
}

function getAllRoles() {
    fetch(`${url}/roles/`)
        .then(res => res.json())
        .then(data => {
            loadRole(data);
            userRoles = data;
        })
}

function loadRole(listAllRoles) {
    let res = ``;

    for (let role of listAllRoles) {
        res +=
            `<option value=${role.id}>${role.name}</option>`
    }
    document.getElementById('rolesNew').innerHTML = res;

}

function loadTable(listAllUsers) {
    let res = ``;

    for (let user of listAllUsers) {
        res +=
            `<tr id="row" >
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.username}</td>
                <td>${user.rolesToString}</td>
                
                <td>
                    <button id="button-edit" class="btn btn-sm btn-primary" type="button"
                    data-bs-toggle="modal" href="#editModal"
                    onclick="editModal(${user.id})">Изменить</button></td>
                <td>
                    <button class="btn btn-sm btn-danger" type="button"
                    data-bs-toggle="modal" data-bs-target="#deleteModal"
                    onclick="deleteModal(${user.id})">Удалить</button></td>
            </tr>`
    }
    document.getElementById('tableBodyAdmin').innerHTML = res;

}

function newUserTab() {
    document.getElementById('rolesNew').value
    document.getElementById('newUserForm').addEventListener('submit', (e) => {
        e.preventDefault()

        fetch('http://localhost:8080/api/users/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({
                firstName: document.getElementById('firstNameNew').value,
                lastName: document.getElementById('lastNameNew').value,
                username: document.getElementById('usernameNew').value,
                password: document.getElementById('passwordNew').value,
                roles: userRoles.filter(elem => elem.id === Number(document.getElementById('rolesNew').value)),



            })
        })
            .then((response) => {
                if (response.ok) {
                    document.getElementById('firstNameNew').value = '';
                    document.getElementById('lastNameNew').value = '';
                    document.getElementById('usernameNew').value = '';
                    document.getElementById('passwordNew').value = '';
                    document.getElementById('rolesNew').value = '';
                   document.getElementById('users-tab').click()

                    getAllUsers();

                }
            })
    })

}


function closeModal() {
    document.querySelectorAll(".btn-close").forEach((btn) => btn.click())
}


function editModal(id) {
    let editId = `${url}/${id}`;
    fetch(`http://localhost:8080/api/users/${id}`, {
        headers: {
            'Accept': 'application/json',
           'Content-Type': 'application/json;charset=UTF-8'

        }
    }).then(res => {
        res.json().then(user => {
           document.getElementById('editId').value = user.id;
            document.getElementById('editFirstName').value = user.firstName;
            document.getElementById('editLastName').value = user.lastName;
            document.getElementById('editUsername').value = user.username;
           document.getElementById('editPassword').value = user.password;
            let res = ``;

            for (let role of userRoles) {
                res += user.roles[0].id === role.id ?

                    `<option value=${role.id} selected>${role.name}</option>`
                    : `<option value=${role.id}>${role.name}</option>`
            }
            document.getElementById('editRole').innerHTML = res;
        })
    });

}


async function editUser() {
   let idValue = document.getElementById('editId').value;
    let firstNameValue = document.getElementById('editFirstName').value;
    let lastNameValue = document.getElementById('editLastName').value;
    let usernameValue = document.getElementById('editUsername').value;
    let passwordValue = document.getElementById('editPassword').value;
    let role = userRoles.filter(elem => elem.id === Number(document.getElementById('editRole').value));


    let user = {
        id: idValue,
        firstName: firstNameValue,
        lastName: lastNameValue,
        username: usernameValue,
        password: passwordValue,
        roles: role
    }
    await fetch(`http://localhost:8080/api/users`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(user)
    });

    closeModal()
   getAllUsers()

}


function deleteModal(id) {
    fetch(`http://localhost:8080/api/users/${id}`, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => {
        res.json().then(user => {
            document.getElementById('deleteId').value = user.id;
            document.getElementById('deleteFiName').value = user.firstName;
            document.getElementById('deleteLastName').value = user.lastName;
            document.getElementById('deleteUsName').value = user.username;
            document.getElementById('deleteRoles').value = user.rolesToString;
        })
    });
}

async function deleteUser() {
    const id = document.getElementById('deleteId').value
    let urlDel = `${url}/${id}`;

    let method = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }
    fetch(`http://localhost:8080/api/users/${id}`, method).then(() => {
        closeModal()
        getAllUsers()
    })

}
getAllUsers();
getAllRoles();




