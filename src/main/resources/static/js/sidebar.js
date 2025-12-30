document.addEventListener("DOMContentLoaded", () => {
    const items = document.querySelectorAll('.sidebar-item');

    // Hiệu ứng Fade-in lần lượt (Staggered Animation)
    items.forEach((item, index) => {
        item.style.opacity = '0';
        item.style.transform = 'translateY(10px)';
        item.style.transition = 'opacity 0.5s ease, transform 0.5s ease';

        // Delay tăng dần theo thứ tự index
        setTimeout(() => {
            item.style.opacity = '1';
            item.style.transform = 'translateY(0)';

            // Sau khi animation xong, xóa style inline để hover CSS hoạt động chuẩn
            setTimeout(() => {
                item.style.transition = '';
                item.style.transform = '';
            }, 500);

        }, index * 50); // Mỗi item hiện cách nhau 50ms
    });
});