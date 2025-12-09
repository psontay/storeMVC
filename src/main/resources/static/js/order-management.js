function showOrderDetail(orderId) {
    fetch(`/api/admin/orders/${orderId}`)
        .then(r => r.json())
        .then(order => {
            document.getElementById('modalOrderId').textContent = order.orderId.substring(0, 8);

            document.getElementById('modalBody').innerHTML = `
                <div class="row g-4">
                    <div class="col-md-6">
                        <p><strong>Khách hàng:</strong> ${order.userEmail}</p>
                        <p><strong>Địa chỉ giao hàng:</strong><br>${order.shippingAddress || '<em class="text-muted">Chưa cung cấp</em>'}</p>
                        <p><strong>Ngày đặt hàng:</strong> ${order.formattedOrderDate}</p>
                    </div>
                    <div class="col-md-6 text-md-end">
                        <h4>Tổng tiền: <span class="text-success">${order.formattedTotalPrice}</span></h4>
                    </div>
                </div>

                <hr class="my-4">

                <form th:action="@{'/admin/orders/' + order.orderId + '/status'}" method="post" class="d-flex gap-3 align-items-center">
                    <input type="hidden" name="${_csrf.parameterName}" th:value="${_csrf.token}">
                    <label class="form-label mb-0 fw-bold">Cập nhật trạng thái:</label>
                    <select name="orderStatus" class="form-select" style="width: auto;">
                        <option value="PENDING"    ${order.orderStatus==='PENDING' ? 'selected' : ''}>Chờ xác nhận</option>
                        <option value="PROCESSING" ${order.orderStatus==='PROCESSING' ? 'selected' : ''}>Đang xử lý</option>
                        <option value="SHIPPED"    ${order.orderStatus==='SHIPPED' ? 'selected' : ''}>Đã giao hàng</option>
                        <option value="DELIVERED"  ${order.orderStatus==='DELIVERED' ? 'selected' : ''}>Đã giao</option>
                        <option value="CANCELLED"  ${order.orderStatus==='CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                    </select>
                    <button type="submit" class="btn btn-success">Cập nhật</button>
                </form>
            `;

            new bootstrap.Modal(document.getElementById('orderDetailModal')).show();
        })
        .catch(() => alert('Không tải được chi tiết đơn hàng'));
}

function closeModal() {
    bootstrap.Modal.getInstance(document.getElementById('orderDetailModal')).hide();
}