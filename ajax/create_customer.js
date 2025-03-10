// Biến toàn cục để lưu danh sách thành phố
let citiesData = [];

// Load danh sách thành phố khi trang mở
$(document).ready(function() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/cities",
        success: function(cities) {
            console.log("Danh sách thành phố:", cities);
            citiesData = cities; // Lưu dữ liệu vào biến toàn cục
            var citySelect = $('#cityId');
            $.each(cities, function(index, city) {
                citySelect.append(
                    '<option value="' + city.id + '">' + city.nameCity + '</option>'
                );
            });
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi tải danh sách thành phố:", status, error);
            alert('Không thể tải danh sách thành phố');
        }
    });
});

// Thêm khách hàng
function createCustomer() {
    var customer = {
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        email: $('#email').val(),
        cityId: $('#cityId').val()
    };

    if (!customer.firstName || !customer.lastName || !customer.email || !customer.cityId) {
        alert('Vui lòng điền đầy đủ thông tin');
        return;
    }

    // Lấy tên thành phố từ select
    var cityName = $('#cityId option:selected').text();
    if (cityName === "Chọn thành phố" || !cityName) {
        alert('Vui lòng chọn một thành phố hợp lệ');
        return;
    }

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/customers",
        contentType: "application/json",
        data: JSON.stringify(customer),
        success: function(response) {
            console.log("Response từ server:", response); // Kiểm tra dữ liệu trả về
            alert('Thêm khách hàng thành công: ' + customer.firstName + ' ' + customer.lastName + ' - Thành phố: ' + cityName);
            window.location.href = 'customers.html';
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi thêm:", status, error);
            alert('Lỗi khi thêm khách hàng: ' + xhr.responseText);
        }
    });
}