// /static/js/management.js
document.addEventListener('DOMContentLoaded', () => {
    const overlay = document.getElementById('modalOverlay');
    const modals = document.querySelectorAll('.modal');

    // 1. Hàm đóng tất cả modal
    function closeAllModals() {
        modals.forEach(m => m.classList.remove('show'));
        overlay.classList.remove('show');
    }

    // 2. Mở modal Create
    document.getElementById('createBtn')?.addEventListener('click', (e) => {
        e.preventDefault(); // Ngăn hành vi mặc định nếu là thẻ a
        document.getElementById('createModal').classList.add('show');
        overlay.classList.add('show');
    });

    // 3. Tự mở modal Edit nếu có (khi render lại trang)
    const editModal = document.getElementById('editModal');
    if (editModal) {
        editModal.classList.add('show');
        overlay.classList.add('show');
    }

    // 4. Các nút đóng (dấu X hoặc nút Cancel)
    document.querySelectorAll('.close').forEach(btn => {
        btn.addEventListener('click', closeAllModals);
    });

    // 5. Logic đóng khi click ra ngoài (Click vào vùng xám)
    // Vì .modal đang nằm ĐÈ lên overlay, nên ta bắt sự kiện click vào chính .modal
    modals.forEach(modal => {
        modal.addEventListener('click', (e) => {
            // Nếu click chính xác vào vùng wrapper (.modal) thì đóng
            // Nếu click vào form bên trong (.modal-content) thì e.target sẽ khác e.currentTarget -> không đóng
            if (e.target === e.currentTarget) {
                closeAllModals();
            }
        });
    });

    // Xóa đoạn code overlay.addEventListener cũ và đoạn stopPropagation cũ đi
    // Vì logic ở mục 5 đã xử lý gọn gàng rồi.
});