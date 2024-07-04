const URL = "http://localhost:8080/api/users";

async function getCurrent() {
    let response = await fetch(`${URL}/current`);
    let data = await response.json();
    return data;
}
async function getUsers() {
    let response = await fetch(URL);
    let data = await response.json();
    return data;
}
async function getUserById(id) {
    let response = await fetch(`${URL}/${id}`);
    let data = await response.json();
    return data;
}
async function getCurrentEmail() {
    let data = await getCurrent();
    return data.email;
}
async function getCurrentRoles() {
    let data = await getCurrent();
    return data.roleList;
}
async function setCurrentEmail(){
    let emailElement = document.getElementById("email");
    emailElement.textContent = await getCurrentEmail();
}
async function getRoles() {
    let response = await fetch(`${URL}/roles`);
    let data = await response.json();
    return data;
}
async function setCurrentRoles() {
    let rolesElement = document.getElementById("roles");
    let roleList = await getCurrentRoles();
    roleList.forEach(role => {
        let span = document.createElement('span');
        span.textContent = role.shortRole;
        rolesElement.appendChild(span);
    });
}
async function setHeader() {
    await setCurrentEmail();
    await setCurrentRoles();
}

async function setUserInfo(){
    let tr = document.getElementById('user_info');
    let user = await getCurrent();
    tr.innerHTML = `
        <tr class="pb-2">
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.roleList.length >= 2 ? `${user.roleList[0].shortRole} ${user.roleList[1].shortRole}` : user.roleList[0].shortRole}</td>
        </tr>
    `;
}

async function setUserPage(){
    await setHeader();
    await setUserInfo();
}

async function getDeleteModal(id){
    let user = await getUserById(id);
    document.getElementById("form2ID").value = user.id;
    document.getElementById("form2fname").value = user.name;
    document.getElementById("form2lname").value = user.lastName;
    document.getElementById("form2age").value = user.age;
    document.getElementById("form2email").value = user.email;
    document.getElementById("form2password").value = user.password;
    let selectRoles = document.getElementById("delete-role-select");
    selectRoles.innerHTML = '';
    let roles = user.roleList;
    roles.forEach(data => {
        selectRoles.insertAdjacentHTML("afterbegin", `<option value="${data.shortRole}">${data.shortRole}</option>`);
    });
    $('#deleteModal').modal('show');
}

async function getEditModal(id) {
    let user = await getUserById(id);
    document.getElementById("form1ID").value = user.id;
    document.getElementById("form1fname").value = user.name;
    document.getElementById("form1lname").value = user.lastName;
    document.getElementById("form1age").value = user.age;
    document.getElementById("form1email").value = user.email;
    document.getElementById("form1password").value = user.password;
    let selectRoles = document.getElementById("edit-role-select");
    selectRoles.innerHTML = '';

    let roles = await getRoles();
    roles.forEach(data => {
        const isSelected = user.roleList.some(userRole => userRole.shortRole === data.shortRole);
        selectRoles.insertAdjacentHTML("afterbegin", `<option ${isSelected ? 'selected' : ''} value="${data.shortRole}">${data.shortRole}</option>`);
    });

    $('#editModal').modal('show');
}

async function editUser() {
    const id = document.getElementById("form1ID").value;
    const name = document.getElementById("form1fname").value;
    const lastName = document.getElementById("form1lname").value;
    const age = document.getElementById("form1age").value;
    const email = document.getElementById("form1email").value;
    const password = document.getElementById("form1password").value;
    const roleList = Array.from(document.getElementById("edit-role-select").selectedOptions)
        .map(option => option.value === 'ADMIN' ? { authority: "ROLE_ADMIN", role: "ROLE_ADMIN", shortRole: "ADMIN" } : { authority: "ROLE_USER", role: "ROLE_USER", shortRole: "USER" });

    const user = { id, name, lastName, age, email, password, roleList };
    const response = await fetch(`${URL}/${id}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    });

    if (response.ok) {
        $('#editModal').modal('hide');
        await setUserTable(); // обновить таблицу пользователей
    } else {
    }
}

async function deleteUser() {
    const id = document.getElementById("form2ID").value;
    const response = await fetch(`${URL}/${id}`, { method: 'DELETE' });

    if (response.ok) {
        $('#deleteModal').modal('hide');
        await setUserTable(); // обновить таблицу пользователей
    } else {
    }
}
async function createUser() {
    const name = document.getElementById("form3fname").value;
    const lastName = document.getElementById("form3lname").value;
    const age = document.getElementById("form3age").value;
    const email = document.getElementById("form3email").value;
    const password = document.getElementById("form3password").value;
    const roleList = Array.from(document.getElementById("create-role-select").selectedOptions)
        .map(option => option.value === 'ADMIN' ? { authority: "ROLE_ADMIN", role: "ROLE_ADMIN", shortRole: "ADMIN" } : { authority: "ROLE_USER", role: "ROLE_USER", shortRole: "USER" });

    const user = { name, lastName, age, email, password, roleList };
    const response = await fetch(`${URL}/`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    });

    if (response.ok) {
        await setUserTable(); // обновить таблицу пользователей
    } else {
    }
}
async function setUserTable(){
    let tbody = document.getElementById("users_info");
    tbody.innerHTML = ''; // очищаем таблицу перед заполнением
    let users = await getUsers();
    users.forEach(user => {
        tbody.insertAdjacentHTML("beforeend", `
            <tr class="pb-2">
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.roleList.length >= 2 ? `${user.roleList[0].shortRole} ${user.roleList[1].shortRole}` : user.roleList[0].shortRole}</td>
                <td>
                    <button type="button" class="edit-btn btn btn-info" data-toggle="modal" onclick="getEditModal(${user.id})">
                        Edit
                    </button>
                </td>
                <td>
                    <button type="button" class="delete-btn btn btn-danger" data-toggle="modal" onclick="getDeleteModal(${user.id})">
                        Delete
                    </button>
                </td>
            </tr>
        `);
    });
    let selectRoles = document.getElementById("create-role-select");
    selectRoles.innerHTML = '';

    let roles = await getRoles();
    roles.forEach(data => {
        selectRoles.insertAdjacentHTML("afterbegin", `<option value="${data.shortRole}">${data.shortRole}</option>`);
    });
}

async function setAdminPage() {
    await setUserTable();
}

document.addEventListener('DOMContentLoaded', async function() {
    await setUserPage();
    await setAdminPage();
    document.getElementById('editUserForm').addEventListener('submit', async function(event) {
        event.preventDefault(); // предотвратить перезагрузку страницы
        await editUser();
    });
    document.getElementById('deleteUserForm').addEventListener('submit', async function(event) {
        event.preventDefault(); // предотвратить перезагрузку страницы
        await deleteUser();
    });
    document.getElementById('createUserForm').addEventListener('submit', async function(event) {
        event.preventDefault(); // предотвратить перезагрузку страницы
        await createUser();
    });
});

// Добавление обработчика для формы редактирования, чтобы он добавлялся только один раз
