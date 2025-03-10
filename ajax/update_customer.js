const urlParams = new URLSearchParams(window.location.search);
const customerId = urlParams.get('id');

$(document).ready(function() {
    // Load danh sách thành phố
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/cities",
        success: function(cities) {
            console.log("Danh sách thành phố:", cities);
            var citySelect = $('#cityId');
            $.each(cities, function(index, city) {
                citySelect.append(
                    '<option value="' + city.id + '">' + city.nameCity + '</option>'
                );
            });
            // Sau khi load thành phố, load thông tin khách hàng
            loadCustomerData();
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi tải danh sách thành phố:", status, error);
            alert('Không thể tải danh sách thành phố');
        }
    });
});

// Load thông tin khách hàng theo id
function loadCustomerData() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/customers/" + customerId,
        success: function(customer) {
            console.log("Thông tin khách hàng:", customer);
            $('#customerId').val(customer.id);
            $('#firstName').val(customer.firstName);
            $('#lastName').val(customer.lastName);
            $('#email').val(customer.email);
            $('#cityId').val(customer.cityId); // Chọn thành phố tương ứng
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi tải khách hàng:", status, error);
            alert('Lỗi khi tải thông tin khách hàng');
        }
    });
}

// Cập nhật khách hàng
function updateCustomer() {
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

    $.ajax({
        type: "PUT",
        url: "http://localhost:8080/api/customers/" + customerId,
        contentType: "application/json",
        data: JSON.stringify(customer),
        success: function(response) {
            var cityName = $('#cityId option:selected').text();
            alert('Cập nhật khách hàng thành công: ' + customer.firstName + ' ' + customer.lastName + ' - Thành phố: ' + cityName);
            window.location.href = 'customers.html';
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi cập nhật:", status, error);
            alert('Lỗi khi cập nhật khách hàng: ' + xhr.responseText);
        }
    });
}