function toggleCheck(element) {
    const items = document.querySelectorAll('.box-type-keyword-below__list--item');

    // Kiểm tra nếu phần tử đang được chọn
    if (element.classList.contains('selected')) {
        // Nếu đã chọn thì bỏ chọn
        element.classList.remove('selected');
    } else {
        // Nếu chưa chọn thì bỏ chọn các phần tử khác và chọn phần tử hiện tại
        items.forEach(item => item.classList.remove('selected'));
        element.classList.add('selected');
    }
}