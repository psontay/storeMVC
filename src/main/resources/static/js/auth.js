const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});

// Thymeleaf inject biến
let errorFromServer = window.errorFromServer === true;
let successFromServer = window.successFromServer === true;

// nếu REGISTER thành công → chuyển sang sign-in
// Nếu có lỗi đăng ký → Hiện form Sign Up
if (errorFromServer === true) {
    container.classList.add("right-panel-active");
}

// Nếu đăng ký thành công → Hiện form Sign In
if (successFromServer === true) {
    container.classList.remove("right-panel-active");
}

