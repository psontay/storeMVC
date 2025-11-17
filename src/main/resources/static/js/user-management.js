document.addEventListener('DOMContentLoaded', () => {
    const createBtn = document.getElementById('createUserBtn');
    const userFormCard = document.getElementById('userFormCard');
    const cancelFormBtn = document.getElementById('cancelFormBtn');
    const userForm = document.getElementById('userForm');
    const userTableBody = document.getElementById('userTableBody');
    const searchInput = document.getElementById('searchUserInput');

    let editUserId = null;

    // Show form
    createBtn.addEventListener('click', () => {
        editUserId = null;
        userFormCard.style.display = 'block';
        userForm.reset();
        document.getElementById('formTitle').textContent = "Tạo người dùng";
    });

    // Cancel form
    cancelFormBtn.addEventListener('click', () => {
        userFormCard.style.display = 'none';
    });

    // Load users
    async function loadUsers(query="") {
        try {
            const res = await fetch('/admin/users?search=' + encodeURIComponent(query));
            const users = await res.json();
            userTableBody.innerHTML = users.map((u,i) => `
                <tr>
                    <td>${i+1}</td>
                    <td>${u.username}</td>
                    <td>${u.email}</td>
                    <td>${u.role}</td>
                    <td>
                        <button class="btn small editBtn" data-id="${u.id}">Sửa</button>
                        <button class="btn small danger deleteBtn" data-id="${u.id}">Xóa</button>
                    </td>
                </tr>
            `).join('');

            // Attach edit/delete events
            document.querySelectorAll('.editBtn').forEach(btn => {
                btn.addEventListener('click', () => editUser(btn.dataset.id));
            });
            document.querySelectorAll('.deleteBtn').forEach(btn => {
                btn.addEventListener('click', () => deleteUser(btn.dataset.id));
            });

        } catch (e) {
            console.error('Failed to load users', e);
        }
    }

    // Search
    searchInput.addEventListener('input', (e) => {
        loadUsers(e.target.value);
    });

    // Submit form (create/update)
    userForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(userForm);
        const data = Object.fromEntries(formData.entries());

        try {
            let url = '/admin/users';
            let method = 'POST';
            if(editUserId){
                url += '/' + editUserId;
                method = 'PUT';
            }
            const res = await fetch(url, {
                method,
                headers: {'Content-Type':'application/json'},
                body: JSON.stringify(data)
            });

            if(res.ok){
                userFormCard.style.display = 'none';
                loadUsers();
            } else {
                alert('Thất bại');
            }
        } catch (e) {
            console.error(e);
        }
    });

    // Edit
    async function editUser(id){
        try {
            const res = await fetch('/admin/users/' + id);
            const u = await res.json();
            editUserId = id;
            document.getElementById('username').value = u.username;
            document.getElementById('email').value = u.email;
            document.getElementById('role').value = u.role;
            document.getElementById('formTitle').textContent = "Sửa người dùng";
            userFormCard.style.display = 'block';
        } catch (e) {
            console.error(e);
        }
    }

    // Delete
    async function deleteUser(id){
        if(!confirm("Bạn có chắc muốn xóa?")) return;
        try {
            const res = await fetch('/admin/users/' + id, {method:'DELETE'});
            if(res.ok){
                loadUsers();
            }
        } catch(e){
            console.error(e);
        }
    }

    loadUsers();
});
