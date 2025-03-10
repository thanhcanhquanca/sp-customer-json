function showAllCustomer() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/customers",
        cache: false, // Ngăn chặn cache của AJAX
        success: function(data) {
            console.log("Dữ liệu nhận được:", data);
            var customerList = $('#customerList');

            // Kiểm tra xem #customerList có tồn tại không
            if (!customerList.length) {
                console.error("Không tìm thấy phần tử #customerList trong HTML");
                return;
            }

            customerList.empty(); // Xóa dữ liệu cũ trong tbody

            if (data.length === 0) {
                customerList.append('<tr><td colspan="6">Không có khách hàng nào.</td></tr>');
                return;
            }

            $.each(data, function(index, customer) {
                customerList.append(
                    '<tr>' +
                    '<td>' + customer.id + '</td>' +
                    '<td>' + customer.firstName + '</td>' +
                    '<td>' + customer.lastName + '</td>' +
                    '<td>' + customer.email + '</td>' +
                    '<td>' + customer.cityName + '</td>' +
                    '<td>' +
                    '<button onclick="window.location.href=\'update-customer.html?id=' + customer.id + '\'">Sửa</button>' +
                    '<button onclick="deleteCustomer(' + customer.id + ')">Xóa</button>' +
                    '</td>' +
                    '</tr>'
                );
            });
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi gọi API:", status, error);
            $('#customerList').html('<tr><td colspan="6">Lỗi khi tải dữ liệu.</td></tr>');
        }
    });
}

// Xóa khách hàng
function deleteCustomer(id) {
    if (confirm('Bạn có chắc muốn xóa khách hàng này?')) {
        $.ajax({
            type: "DELETE",
            url: "http://localhost:8080/api/customers/" + id,
            success: function() {
                showAllCustomer(); // Tải lại danh sách sau khi xóa
            },
            error: function(xhr, status, error) {
                console.error("Lỗi khi xóa:", status, error);
                alert('Lỗi khi xóa khách hàng');
            }
        });
    }
}

// Gọi hàm khi trang tải
$(document).ready(function() {
    showAllCustomer();
});