// ==================== MODAL THÊM GIỎ HÀNG (Dùng chung Home & Detail) ====================
let currentProductId = null;
let currentProductName = null;

document.addEventListener('DOMContentLoaded', function () {
    // 1. XỬ LÝ MODAL (Code cũ của bạn)
    const modal = document.getElementById('addToCartModal');
    if (modal) {
        modal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;

            // Lấy dữ liệu từ Data Attribute
            currentProductId = button.getAttribute('data-product-id');
            currentProductName = button.getAttribute('data-product-name');
            const productImageAttr = button.getAttribute('data-product-image');

            // Cập nhật Tên
            document.getElementById('modalProductName').textContent = currentProductName;

            // XỬ LÝ ẢNH
            const modalImg = document.getElementById('modalProductImage');
            if (productImageAttr && productImageAttr.trim() !== "") {
                let finalSrc = productImageAttr;
                if (!productImageAttr.startsWith('http') && !productImageAttr.startsWith('/')) {
                    finalSrc = '/' + productImageAttr;
                }
                modalImg.src = finalSrc;
            } else {
                const imgInCard = button.closest('.product-card')?.querySelector('img');
                modalImg.src = imgInCard ? imgInCard.src : 'https://via.placeholder.com/80';
            }

            // Reset số lượng
            document.getElementById('quantityInput').value = 1;
        });
    }

    // 2. FIX QUAN TRỌNG: KẾT NỐI NÚT CONFIRM VỚI HÀM XỬ LÝ
    // (Lần trước bạn bị thiếu đoạn này nên nút bấm không chạy)
    const btnConfirm = document.getElementById('btnConfirmAddToCart');
    if (btnConfirm) {
        btnConfirm.addEventListener('click', function() {
            confirmAddToCart();
        });
    }
});


// Hàm xử lý logic gọi API
async function confirmAddToCart() {
    if (!currentProductId) return alert('Error: Product not found!');

    const quantity = parseInt(document.getElementById('quantityInput').value) || 1;

    // Lấy token từ thẻ meta (Giờ HTML đã có đủ 2 thẻ nên sẽ không bị lỗi nữa)
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');

    if (!tokenMeta || !headerMeta) {
        return alert('Lỗi bảo mật CSRF: Thiếu thẻ meta trong HTML header!');
    }

    const token = tokenMeta.content;
    const header = headerMeta.content;

    try {
        const res = await fetch('/api/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
            },
            body: JSON.stringify({ productId: currentProductId, quantity })
        });

        if (res.ok) {
            const data = await res.json();
            const badge = document.querySelector('.cart-badge');
            if (badge) {
                badge.textContent = data.totalItems;
                badge.style.display = data.totalItems > 0 ? 'inline' : 'none';
            }
            // Đóng modal đúng cách
            const modalEl = document.getElementById('addToCartModal');
            const modalInstance = bootstrap.Modal.getInstance(modalEl);
            if (modalInstance) modalInstance.hide();

            showToast(`Adding "${currentProductName}" into cart!`, 'success');
        } else {
            alert('Adding failed!');
        }
    } catch (err) {
        console.error(err); // Log lỗi ra console để debug nếu cần
        alert('Lỗi kết nối!');
    }
}

// ... Phần còn lại của file giữ nguyên ...

// ... Các hàm changeQuantity, confirmAddToCart giữ nguyên như cũ ...

function changeQuantity(delta) {
    const input = document.getElementById('quantityInput');
    let value = parseInt(input.value) || 1;
    value += delta;
    if (value < 1) value = 1;
    if (value > 99) value = 99;
    input.value = value;
}


// ==================== TRANG GIỎ HÀNG (/cart) ====================
async function updateQuantity(button, delta) {
    const row = button.closest('.card-body');
    const input = row.querySelector('input[type="text"], input[type="number"]');
    if (!input) return;

    let qty = parseInt(input.value) + delta;
    if (qty < 1) return;

    input.value = qty;

    const productId = button.getAttribute('data-product-id');
    await sendUpdate(productId, qty);
}

async function removeFromCart(button) {
    if (!confirm('Xóa sản phẩm này khỏi giỏ hàng?')) return;

    const productId = button.getAttribute('data-product-id');
    await sendRemove(productId);
}

async function sendUpdate(productId, quantity) {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    try {
        await fetch('/api/cart/update', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', [header]: token },
            body: JSON.stringify({ productId, quantity })
        });
        location.reload();
    } catch (err) {
        alert('Update failed!');
        location.reload();
    }
}

async function sendRemove(productId) {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    try {
        await fetch(`/api/cart/remove/${productId}`, { method: 'DELETE', headers: { [header]: token } });
        location.reload();
    } catch (err) {
        alert('Xóa thất bại!');
    }
}

// Toast đẹp
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-bg-${type === 'success' ? 'success' : 'danger'} border-0`;
    toast.style.cssText = 'position:fixed;top:20px;right:20px;z-index:9999;';
    toast.innerHTML = `<div class="d-flex"><div class="toast-body">${message}</div><button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button></div>`;
    document.body.appendChild(toast);
    new bootstrap.Toast(toast, { delay: 3000 }).show();
    setTimeout(() => toast.remove(), 4000);
}
function updateTotalDisplay() {
    const checkboxes = document.querySelectorAll('.item-checkbox');
    let total = 0;
    let count = 0;
    let allChecked = true;

    checkboxes.forEach(cb => {
        if (cb.checked) {
            total += parseFloat(cb.getAttribute('data-subtotal'));
            count++;
        } else {
            allChecked = false;
        }
    });

    const selectAllCb = document.getElementById('selectAll');
    if(checkboxes.length > 0) selectAllCb.checked = allChecked;

    // Hiển thị
    document.getElementById('displayTotal').innerText = formatter.format(total);
    document.getElementById('selectedCount').innerText = count;

    // --- CẬP NHẬT TRẠNG THÁI NÚT (SỬA ĐOẠN NÀY) ---
    const checkoutBtn = document.getElementById('checkoutBtn');
    const deleteBtn = document.getElementById('deleteSelectedBtn'); // Lấy nút xóa

    if (count === 0) {
        checkoutBtn.disabled = true;
        if(deleteBtn) deleteBtn.disabled = true; // Khóa nút xóa
    } else {
        checkoutBtn.disabled = false;
        if(deleteBtn) deleteBtn.disabled = false; // Mở nút xóa
    }
}