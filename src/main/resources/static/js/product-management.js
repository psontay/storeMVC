document.getElementById('createProductBtn')?.addEventListener('click', () => {
    const card = document.getElementById('createProductCard');
    card.style.display = 'block';
    card.scrollIntoView({ behavior: 'smooth' });
});

document.querySelectorAll('.cancelBtn').forEach(btn => {
    btn.addEventListener('click', () => {
        btn.closest('.card').style.display = 'none';
    });
});