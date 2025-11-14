<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Tour Hoi An</title>

    <!-- CSS: Moved to root -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/place.css" rel="stylesheet">

    <style>
        /* SPINNER STYLES */
        .image-wrapper {
            position: relative;
            height: 400px;
            background: #f8f9fa;
            border-radius: 10px;
            overflow: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .image-wrapper img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            opacity: 0;
            transition: opacity 0.5s ease;
        }
        .image-wrapper.loaded img {
            opacity: 1;
        }
        .spinner {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 50px;
            height: 50px;
            border: 5px solid #e9ecef;
            border-top: 5px solid #8ab92d;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            z-index: 2;
        }
        .image-wrapper.loaded .spinner {
            opacity: 0;
            transform: translate(-50%, -50%) scale(0.8);
            transition: all 0.4s ease;
        }
        @keyframes spin {
            to { transform: translate(-50%, -50%) rotate(360deg); }
        }
        .image-wrapper:not(.loaded) .spinner {
            opacity: 1 !important;
        }
        .gallery-img {
            height: 150px;
            object-fit: cover;
            border-radius: 8px;
        }
    </style>
</head>
<body>

<!-- Navigation -->
<nav class="navbar fixed-top navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/index">Tour Hoi An</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/index">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/about">About</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Contact</a></li>
            </ul>
        </div>
    </div>
</nav>

<!-- Page Content -->
<div class="container mt-5 pt-4">
    <div id="place-content">
        <!-- Dynamic content loaded here -->
    </div>
</div>

<!-- Footer -->
<footer class="py-3 bg-dark text-white mt-5">
    <div class="container">
        <p class="m-0 text-center">Copyright © Tour Hoi An</p>
    </div>
</footer>

<!-- JS: Moved to root -->
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        // Hai dòng này là JSP EL, chúng ĐÚNG vì nằm trong dấu " "
        const contextPath = "${pageContext.request.contextPath}";
        const slug = "${slug}";

        if (!slug || slug === 'null') {
            document.getElementById('place-content').innerHTML = '<p class="text-center text-danger">Không tìm thấy địa điểm.</p>';
            return;
        }

        // Dùng phép cộng chuỗi (dấu +)
        fetch(contextPath + "/api/diadiem/" + slug)
            .then(function(r) { 
                return r.ok ? r.json() : Promise.reject("Not found"); 
            })
            .then(function(data) {
                const mainImage = data.gallery && data.gallery[0] ? contextPath + data.gallery[0].imageUrl : '';

                // SỬA LỖI 1: Viết lại galleryHtml bằng phép cộng chuỗi
                const galleryHtml = data.gallery && data.gallery.length > 1
                    ? data.gallery.slice(1).map(function(img) {
                        return (
                            '<div class="col-md-3 col-sm-6 mb-4">' +
                                '<a href="' + contextPath + img.imageUrl + '" target="_blank">' +
                                '<div class="image-wrapper">' +
                                        '<div class="spinner"></div>' +
                                        '<img src="' + contextPath + img.imageUrl + '"' +
                                             ' class="img-fluid gallery-img"' +
                                             ' onload="this.parentNode.classList.add(\'loaded\')"' +
                                             ' onerror="this.parentNode.classList.add(\'loaded\')">' +
                                    '</div>' +
                                '</a>' +
                            '</div>'
                        );
                    }).join('')
                    : '<p class="text-muted">Không có ảnh phụ.</p>';
                
                // SỬA LỖI 2: Viết lại html bằng phép cộng chuỗi
                const html =
                    '<h1 class="mt-4 mb-3">' + (data.name || 'Không có tên') + '<small> ' + (data.categoryName || '') + '</small></h1>' +
                    '<ol class="breadcrumb">' +
                        '<li class="breadcrumb-item"><a href="' + contextPath + '/index">Home</a></li>' +
                        '<li class="breadcrumb-item active">' + (data.name || 'Địa điểm') + '</li>' +
                    '</ol>' +
                    '<div class="row">' +
                        '<div class="col-md-8">' +
                            '<div class="image-wrapper">' +
                                '<div class="spinner"></div>' +
                                (mainImage ? '' : '<div class="p-5 text-center text-muted">Không có ảnh chính</div>') +
                                (mainImage ? '<img src="' + mainImage + '" class="img-fluid" onload="this.parentNode.classList.add(\'loaded\')" onerror="this.parentNode.classList.add(\'loaded\')">' : '') +
                            '</div>' +
                        '</div>' +
                        '<div class="col-md-4">' +
                            '<h3 class="my-3">Mô tả</h3>' +
                            '<p>' + (data.description || 'Không có mô tả.') + '</p>' +
                            '<h3 class="my-3">Thông tin</h3>' +
                            '<ul>' +
                                '<li><strong>Danh mục:</strong> ' + (data.categoryName || 'Không rõ') + '</li>' +
                                '<li><strong>Tọa độ:</strong> ' + data.latitude + ', ' + data.longitude + '</li>' +
                            '</ul>' +
                        '</div>' +
                    '</div>' +
                    '<h3 class="my-4">Ảnh liên quan</h3>' +
                    '<div class="row">' + galleryHtml + '</div>';

                document.getElementById('place-content').innerHTML = html;
            })
            .catch(function(err) {
                document.getElementById('place-content').innerHTML = '<div class="alert alert-danger">Lỗi: ' + err + '</div>';
            });
    });
</script>
</body>
</html>