// /js/user-management.js

document.addEventListener("DOMContentLoaded", () => {
    const createUserBtn = document.getElementById('createUserBtn');
    const createUserFormCard = document.getElementById('createUserFormCard');
    const cancelBtns = document.querySelectorAll('.cancelBtn');

    // Hiển thị form tạo user
    createUserBtn.addEventListener('click', () => {
        createUserFormCard.style.display = 'block';
    });

    // Ẩn form khi click Cancel
    cancelBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            createUserFormCard.style.display = 'none';
        });
    });

    // Optional: focus vào username khi mở form
    createUserBtn.addEventListener('click', () => {
        const usernameInput = createUserFormCard.querySelector('input[name="username"]');
        if (usernameInput) usernameInput.focus();
    });
});
