document.addEventListener('DOMContentLoaded', function () {
    const createBtn = document.getElementById('createSupplierBtn');
    const createCard = document.getElementById('createSupplierCard');

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