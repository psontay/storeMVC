// ==================== MODAL THÊM GIỎ HÀNG (trang chủ) ====================
let currentProductId = null;
let currentProductName = null;

document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('addToCartModal');
    if (modal) {
        modal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            currentProductId = button.getAttribute('data-product-id');
            currentProductName = button.getAttribute('data-product-name');

            document.getElementById('modalProductName').textContent = currentProductName;
            const img = button.closest('.product-card')?.querySelector('img');
            document.getElementById('modalProductImage').src = img ? img.src : '';

            document.getElementById('quantityInput').value = 1;
        });
    }
});

function changeQuantity(delta) {
    const input = document.getElementById('quantityInput');
    let value = parseInt(input.value) || 1;
    value += delta;
    if (value < 1) value = 1;
    if (value > 99) value = 99;
    input.value = value;
}

async function confirmAddToCart() {
    if (!currentProductId) return alert('Error: Product not found!');

    const quantity = parseInt(document.getElementById('quantityInput').value) || 1;
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

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
            bootstrap.Modal.getInstance(document.getElementById('addToCartModal')).hide();
            showToast(`Adding "${currentProductName}" into cart!`, 'success');
        } else {
            alert('Adding failed!');
        }
    } catch (err) {
        alert('Lỗi kết nối!');
    }
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