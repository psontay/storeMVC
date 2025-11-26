document.addEventListener('DOMContentLoaded', function () {
    const createBtn = document.getElementById('createCategoryBtn');
    const createCard = document.getElementById('createCategoryCard');

    if (createBtn && createCard) {
        createBtn.addEventListener('click', () => {
            createCard.style.display = 'block';
            createCard.scrollIntoView({ behavior: 'smooth' });
        });
    }

    document.querySelectorAll('.cancelBtn').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.closest('.card').style.display = 'none';
        });
    });
});