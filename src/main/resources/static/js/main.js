let currentType = 1;
let currentPageNum = 1;
let pageSize = 10;
let totalRecords = 0;
let totalPages = 1;

function switchTab(type) {
    currentType = type;
    currentPageNum = 1;
    const tabs = document.querySelectorAll('.tab-item');
    tabs.forEach((tab, index) => {
        if (index + 1 === type) {
            tab.classList.add('active');
        } else {
            tab.classList.remove('active');
        }
    });
    loadCategoryList(type);
}

function openAddDialog(type) {
    document.getElementById('categoryType').value = type;
    document.getElementById('dialogTitle').textContent = type === 1 ? '新增菜品分类' : '新增套餐分类';
    document.getElementById('categoryForm').reset();
    document.getElementById('categorySort').value = 1;
    document.getElementById('addCategoryDialog').classList.add('show');
}

function closeDialog() {
    document.getElementById('addCategoryDialog').classList.remove('show');
    window.currentEditId = null;
    document.getElementById('categoryForm').reset();
}

function saveCategory() {
    const editId = window.currentEditId;

    const formData = {
        type: parseInt(document.getElementById('categoryType').value),
        name: document.getElementById('categoryName').value.trim(),
        sort: parseInt(document.getElementById('categorySort').value)
    };

    if (!formData.name) {
        alert('请输入分类名称');
        return;
    }

    let url = '/category';
    let method = 'POST';

    if (editId) {
        formData.id = editId;
        method = 'PUT';
    }

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(result => {
            if (result.code === 1) {
                alert(editId ? '修改分类成功' : '新增分类成功');
                closeDialog();
                loadCategoryList(currentType);
                window.currentEditId = null;
            } else {
                alert(result.msg || (editId ? '修改分类失败' : '新增分类失败'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('网络错误，请稍后重试');
        });
}

function saveAndContinue() {
    const formData = {
        type: parseInt(document.getElementById('categoryType').value),
        name: document.getElementById('categoryName').value.trim(),
        sort: parseInt(document.getElementById('categorySort').value)
    };

    if (!formData.name) {
        alert('请输入分类名称');
        return;
    }

    fetch('/category', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(result => {
            if (result.code === 1) {
                alert('新增分类成功，请继续添加');
                document.getElementById('categoryForm').reset();
                document.getElementById('categorySort').value = 1;
                loadCategoryList(currentType);
            } else {
                alert(result.msg || '新增分类失败');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('网络错误，请稍后重试');
        });
}

function loadCategoryList(type) {
    console.log('加载分类列表，type:', type, 'page:', currentPageNum, 'pageSize:', pageSize);

    fetch(`/category/page?page=${currentPageNum}&pageSize=${pageSize}&type=${type}`)
        .then(response => response.json())
        .then(result => {
            console.log('查询结果:', result);

            if (result.code === 1 && result.data) {
                renderCategoryTable(result.data.records);
                updatePagination(result.data.total, result.data.size, result.data.current, result.data.pages);
            } else {
                console.error('查询失败:', result);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function renderCategoryTable(categories) {
    const tbody = document.getElementById('categoryTableBody');
    tbody.innerHTML = '';

    if (categories.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; color: #999;">暂无数据</td></tr>';
        return;
    }

    categories.forEach(category => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${category.name}</td>
            <td>${category.sort}</td>
            <td>
                <button class="btn btn-primary" style="padding: 6px 12px; font-size: 12px; margin-right: 8px;" onclick='editHandle(${JSON.stringify(category)})'>修改</button>
                <button class="btn btn-danger" style="padding: 6px 12px; font-size: 12px;" onclick="deleteCategory('${category.id}')">删除</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function deleteCategory(id) {
    console.log('准备删除分类，ID:', id, '类型:', typeof id);

    if (confirm('确定要删除该分类吗？')) {
        console.log('用户确认删除，开始发送DELETE请求');

        fetch(`/category/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                console.log('响应状态:', response.status);
                return response.json();
            })
            .then(result => {
                console.log('返回结果:', result);

                if (result.code === 1) {
                    alert('删除成功');
                    loadCategoryList(currentType);
                } else {
                    alert(result.msg || '删除失败');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('网络错误，请稍后重试：' + error.message);
            });
    } else {
        console.log('用户取消删除');
    }
}

function getCategoryList(type) {
    fetch(`/category/list?type=${type}`)
        .then(response => response.json())
        .then(result => {
            if (result.code === 1) {
                const select = document.getElementById('categoryId');
                select.innerHTML = '<option value="">请选择分类</option>';
                result.data.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.id;
                    option.textContent = category.name;
                    select.appendChild(option);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function handleAvatarSuccess(response, file) {
    if (response.code === 1) {
        window.uploadedImageName = response.data;
        const imgPreview = document.getElementById('imgPreview');
        imgPreview.src = `/common/download?name=${response.data}`;
        imgPreview.style.display = 'block';
    } else {
        alert('图片上传失败');
    }
}

function uploadImage(file) {
    return new Promise((resolve, reject) => {
        const formData = new FormData();
        formData.append('file', file);

        fetch('/common/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(result => {
                if (result.code === 1) {
                    resolve(result.data);
                } else {
                    reject(new Error(result.msg || '上传失败'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                reject(error);
            });
    });
}

function editHandle(category) {
    console.log('编辑分类:', category);

    document.getElementById('categoryType').value = category.type;
    document.getElementById('categoryName').value = category.name;
    document.getElementById('categorySort').value = category.sort;

    document.getElementById('dialogTitle').textContent = '修改分类';
    document.getElementById('addCategoryDialog').classList.add('show');

    window.currentEditId = category.id;
}

// ==================== 分页功能 ====================

function updatePagination(total, size, current, pages) {
    totalRecords = total;
    pageSize = size;
    currentPageNum = current;
    totalPages = pages;

    document.getElementById('totalRecords').textContent = totalRecords;
    document.getElementById('currentPage').textContent = currentPageNum;
    document.getElementById('totalPages').textContent = totalPages;
    document.getElementById('pageSizeSelect').value = pageSize;

    updatePaginationButtons();
}

function updatePaginationButtons() {
    document.getElementById('btnFirst').disabled = currentPageNum === 1;
    document.getElementById('btnPrev').disabled = currentPageNum === 1;
    document.getElementById('btnNext').disabled = currentPageNum === totalPages;
    document.getElementById('btnLast').disabled = currentPageNum === totalPages;
}

function changePageSize() {
    pageSize = parseInt(document.getElementById('pageSizeSelect').value);
    currentPageNum = 1;
    loadCategoryList(currentType);
}

function goToFirstPage() {
    if (currentPageNum !== 1) {
        currentPageNum = 1;
        loadCategoryList(currentType);
    }
}

function goToPrevPage() {
    if (currentPageNum > 1) {
        currentPageNum--;
        loadCategoryList(currentType);
    }
}

function goToNextPage() {
    if (currentPageNum < totalPages) {
        currentPageNum++;
        loadCategoryList(currentType);
    }
}

function goToLastPage() {
    if (currentPageNum !== totalPages) {
        currentPageNum = totalPages;
        loadCategoryList(currentType);
    }
}

function jumpToPage() {
    const pageInput = document.getElementById('jumpPage');
    const pageNum = parseInt(pageInput.value);

    if (isNaN(pageNum) || pageNum < 1) {
        alert('请输入有效的页码');
        return;
    }

    if (pageNum > totalPages) {
        alert(`最大页码为 ${totalPages}`);
        return;
    }

    currentPageNum = pageNum;
    pageInput.value = '';
    loadCategoryList(currentType);
}

document.addEventListener('DOMContentLoaded', function() {
    const navItems = document.querySelectorAll('.nav-item a');
    const pageContents = document.querySelectorAll('.page-content');

    loadCategoryList(currentType);

    navItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();

            navItems.forEach(nav => nav.parentElement.classList.remove('active'));
            this.parentElement.classList.add('active');

            const page = this.getAttribute('data-page');
            pageContents.forEach(content => {
                content.classList.remove('active');
            });

            if (page === 'category') {
                document.getElementById('page-category').classList.add('active');
                loadCategoryList(currentType);
            } else if (page === 'dish') {
                document.getElementById('page-dish').classList.add('active');
                loadDishList(1);
            } else if (page === 'setmeal') {
                document.getElementById('page-setmeal').classList.add('active');
                loadSetmealList(1);
            } else if (page === 'orderDetail') {
                document.getElementById('page-orderDetail').classList.add('active');
                loadOrderList(1);
            }
        });
    });

    window.onclick = function(event) {
        const modal = document.getElementById('addCategoryDialog');
        if (event.target === modal) {
            closeDialog();
        }

        const dishModal = document.getElementById('editDishDialog');
        if (event.target === dishModal) {
            closeEditDishDialog();
        }
    };
});

// ==================== 菜品管理功能 ====================

function loadDishList(page, pageSize = 10, name = '') {
    console.log('加载菜品列表，page:', page, 'pageSize:', pageSize, 'name:', name);

    fetch(`/dish/page?page=${page}&pageSize=${pageSize}&name=${name}`)
        .then(response => response.json())
        .then(result => {
            console.log('菜品查询结果:', result);

            if (result.code === 1 && result.data) {
                renderDishTable(result.data.records);
            } else {
                console.error('查询失败:', result);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function renderDishTable(dishes) {
    const tbody = document.querySelector('#page-dish .data-table tbody');
    tbody.innerHTML = '';

    if (!dishes || dishes.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #999;">暂无数据</td></tr>';
        return;
    }

    dishes.forEach(dish => {
        const row = document.createElement('tr');
        const statusText = dish.status === 1 ? '启售' : '停售';
        const statusClass = dish.status === 1 ? 'status-on' : 'status-off';

        row.innerHTML = `
            <td>${dish.name}</td>
            <td><img src="/common/download?name=${dish.image}" alt="${dish.name}" style="width: 80px; height: 60px; object-fit: cover;"></td>
            <td>${dish.categoryName || '-'}</td>
            <td>¥${(dish.price / 100).toFixed(2)}</td>
            <td><span class="${statusClass}">${statusText}</span></td>
            <td>
                <button class="btn btn-primary" style="padding: 4px 10px; font-size: 12px; margin-right: 5px;" onclick="editDish(${dish.id})">修改</button>
                <button class="btn ${dish.status === 1 ? 'btn-danger' : 'btn-success'}" style="padding: 4px 10px; font-size: 12px;" onclick="toggleDishStatus(${dish.id}, ${dish.status})">
                    ${dish.status === 1 ? '停售' : '启售'}
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function openAddDishDialog() {
    console.log('打开新增菜品对话框');

    document.querySelector('#editDishDialog .modal-header h3').textContent = '新增菜品';

    document.getElementById('dishForm').reset();
    document.getElementById('dishId').value = '';
    document.getElementById('flavorContainer').innerHTML = '';
    document.getElementById('dishImagePreview').style.display = 'none';
    window.currentDishImage = null;

    loadCategoryOptions();

    document.getElementById('editDishDialog').classList.add('show');
}

// ... existing code ...
// ... existing code ...
function editDish(id) {
    console.log('编辑菜品，ID:', id);

    document.querySelector('#editDishDialog .modal-header h3').textContent = '修改菜品';

    fetch(`/dish/${id}`)
        .then(response => {
            console.log('响应状态:', response.status);
            return response.json();
        })
        .then(result => {
            console.log('完整返回结果:', JSON.stringify(result));

            if (result.code === 1 && result.data) {
                const dish = result.data;

                document.getElementById('dishId').value = dish.id || '';
                document.getElementById('dishName').value = dish.name || '';
                document.getElementById('dishCategory').value = dish.categoryId || '';
                document.getElementById('dishPrice').value = dish.price || '';
                document.getElementById('dishCode').value = dish.code || '';
                document.getElementById('dishDescription').value = dish.description || '';

                if (dish.image) {
                    document.getElementById('dishImagePreview').src = `/common/download?name=${dish.image}`;
                    document.getElementById('dishImagePreview').style.display = 'block';
                    window.currentDishImage = dish.image;
                }

                loadCategoryOptions();

                renderFlavors(dish.flavors || []);

                document.getElementById('editDishDialog').classList.add('show');
            } else {
                alert('该菜品数据不存在或已被删除\n\n建议:\n1. 刷新页面重新加载列表\n2. 如仍无法修改,请删除后重新添加');
                console.error('返回结果不符合预期:', result);

                setTimeout(() => {
                    loadDishList(1);
                }, 1500);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('网络错误，请稍后重试: ' + error.message);
        });
}
// ... existing code ...

// ... existing code ...

function closeEditDishDialog() {
    document.getElementById('editDishDialog').classList.remove('show');
    document.getElementById('dishForm').reset();
    document.getElementById('flavorContainer').innerHTML = '';
    document.getElementById('dishImagePreview').style.display = 'none';
    window.currentDishImage = null;
}

function loadCategoryOptions() {
    fetch('/category/list?type=1')
        .then(response => response.json())
        .then(result => {
            if (result.code === 1 && result.data) {
                const select = document.getElementById('dishCategory');
                select.innerHTML = '<option value="">请选择分类</option>';
                result.data.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.id;
                    option.textContent = category.name;
                    select.appendChild(option);
                });
            }
        })
        .catch(error => {
            console.error('加载分类失败:', error);
        });
}

function previewDishImage(input) {
    if (input.files && input.files[0]) {
        const file = input.files[0];

        uploadImage(file)
            .then(fileName => {
                if (fileName) {
                    window.currentDishImage = fileName;
                    document.getElementById('dishImagePreview').src = `/common/download?name=${fileName}`;
                    document.getElementById('dishImagePreview').style.display = 'block';
                }
            })
            .catch(error => {
                console.error('上传图片失败:', error);
                alert('图片上传失败');
            });
    }
}

function renderFlavors(flavors) {
    const container = document.getElementById('flavorContainer');
    container.innerHTML = '';

    flavors.forEach((flavor, index) => {
        addFlavorItem(flavor.name, flavor.value, index);
    });
}

function addFlavorItem(name = '', value = '', index = null) {
    const container = document.getElementById('flavorContainer');
    const flavorIndex = index !== null ? index : container.children.length;

    const flavorDiv = document.createElement('div');
    flavorDiv.className = 'flavor-item';
    flavorDiv.style.cssText = 'border: 1px solid #ddd; padding: 10px; margin-bottom: 10px; border-radius: 4px; position: relative;';

    flavorDiv.innerHTML = `
        <button type="button" onclick="this.parentElement.remove()" style="position: absolute; top: 5px; right: 5px; background: #ff4d4f; color: white; border: none; border-radius: 50%; width: 24px; height: 24px; cursor: pointer;">×</button>
        <div style="margin-bottom: 8px;">
            <label style="display: block; margin-bottom: 4px; font-size: 14px;">口味名称:</label>
            <input type="text" class="flavor-name" value="${name}" placeholder="例如:辣度" style="width: 100%; padding: 6px 10px; border: 1px solid #ddd; border-radius: 4px;">
        </div>
        <div>
            <label style="display: block; margin-bottom: 4px; font-size: 14px;">口味值:</label>
            <input type="text" class="flavor-value" value='${value}' placeholder='例如:["不辣","微辣","中辣","特辣"]' style="width: 100%; padding: 6px 10px; border: 1px solid #ddd; border-radius: 4px;">
        </div>
    `;

    container.appendChild(flavorDiv);
}

function addFlavor() {
    addFlavorItem();
}

function collectFlavors() {
    const flavors = [];
    const flavorItems = document.querySelectorAll('.flavor-item');

    flavorItems.forEach(item => {
        const name = item.querySelector('.flavor-name').value.trim();
        const valueStr = item.querySelector('.flavor-value').value.trim();

        if (name && valueStr) {
            try {
                const value = JSON.parse(valueStr);
                flavors.push({
                    name: name,
                    value: Array.isArray(value) ? JSON.stringify(value) : valueStr
                });
            } catch (e) {
                flavors.push({
                    name: name,
                    value: valueStr
                });
            }
        }
    });

    return flavors;
}

function saveDish() {
    const id = document.getElementById('dishId').value;
    const name = document.getElementById('dishName').value.trim();
    const categoryId = document.getElementById('dishCategory').value;
    const price = document.getElementById('dishPrice').value;
    const code = document.getElementById('dishCode').value.trim();
    const description = document.getElementById('dishDescription').value.trim();

    if (!name) {
        alert('请输入菜品名称');
        return;
    }

    if (!categoryId) {
        alert('请选择所属分类');
        return;
    }

    if (!price) {
        alert('请输入菜品价格');
        return;
    }

    const dishData = {
        id: id ? parseInt(id) : undefined,
        name: name,
        categoryId: parseInt(categoryId),
        price: parseInt(price),
        code: code,
        image: window.currentDishImage || '',
        description: description,
        status: 1,
        flavors: collectFlavors()
    };

    const url = '/dish';
    const method = id ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dishData)
    })
        .then(response => response.json())
        .then(result => {
            if (result.code === 1) {
                alert(id ? '修改菜品成功' : '新增菜品成功');
                closeEditDishDialog();
                loadDishList(1);
            } else {
                alert(result.msg || (id ? '修改菜品失败' : '新增菜品失败'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('网络错误，请稍后重试');
        });
}

function toggleDishStatus(id, currentStatus) {
    const newStatus = currentStatus === 1 ? 0 : 1;
    const actionText = newStatus === 1 ? '启售' : '停售';

    if (confirm(`确定要${actionText}该菜品吗？`)) {
        fetch(`/dish/status/${newStatus}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify([id])
        })
            .then(response => response.json())
            .then(result => {
                if (result.code === 1) {
                    alert(`${actionText}成功`);
                    loadDishList(1);
                } else {
                    alert(result.msg || `${actionText}失败`);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('网络错误，请稍后重试');
            });
    }
}

// ==================== 套餐管理功能 ====================

function loadSetmealList(page, pageSize = 10, name = '') {
    console.log('加载套餐列表，page:', page, 'pageSize:', pageSize, 'name:', name);

    fetch(`/setmeal/page?page=${page}&pageSize=${pageSize}&name=${name}`)
        .then(response => response.json())
        .then(result => {
            console.log('套餐查询结果:', result);

            if (result.code === 1 && result.data) {
                renderSetmealTable(result.data.records);
            } else {
                console.error('查询失败:', result);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function renderSetmealTable(setmeals) {
    const tbody = document.querySelector('#page-setmeal .data-table tbody');
    tbody.innerHTML = '';

    if (!setmeals || setmeals.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #999;">暂无数据</td></tr>';
        return;
    }

    setmeals.forEach(setmeal => {
        const row = document.createElement('tr');
        const statusText = setmeal.status === 1 ? '启售' : '停售';
        const statusClass = setmeal.status === 1 ? 'status-on' : 'status-off';

        row.innerHTML = `
            <td>${setmeal.name}</td>
            <td><img src="/common/download?name=${setmeal.image}" alt="${setmeal.name}" style="width: 80px; height: 60px; object-fit: cover;"></td>
            <td>${setmeal.categoryName || '-'}</td>
            <td>¥${(setmeal.price / 100).toFixed(2)}</td>
            <td><span class="${statusClass}">${statusText}</span></td>
            <td>
                <button class="btn btn-primary" style="padding: 4px 10px; font-size: 12px; margin-right: 5px;" onclick="editSetmeal(${setmeal.id})">修改</button>
                <button class="btn ${setmeal.status === 1 ? 'btn-danger' : 'btn-success'}" style="padding: 4px 10px; font-size: 12px;" onclick="toggleSetmealStatus(${setmeal.id}, ${setmeal.status})">
                    ${setmeal.status === 1 ? '停售' : '启售'}
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function editSetmeal(id) {
    alert('编辑套餐功能待开发，套餐ID: ' + id);
}

function toggleSetmealStatus(id, currentStatus) {
    const newStatus = currentStatus === 1 ? 0 : 1;
    const actionText = newStatus === 1 ? '启售' : '停售';

    if (confirm(`确定要${actionText}该套餐吗？`)) {
        fetch(`/setmeal/status/${newStatus}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify([id])
        })
            .then(response => response.json())
            .then(result => {
                if (result.code === 1) {
                    alert(`${actionText}成功`);
                    loadSetmealList(1);
                } else {
                    alert(result.msg || `${actionText}失败`);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('网络错误，请稍后重试');
            });
    }
}

// ==================== 订单明细功能 ====================

function loadOrderList(page, pageSize = 10) {
    console.log('加载订单列表，page:', page, 'pageSize:', pageSize);

    const tbody = document.querySelector('#page-orderDetail .data-table tbody');
    tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #999;">订单功能待开发</td></tr>';
}
function deleteCategory(id) {
    if (confirm('确定要删除该分类吗？')) {
        fetch(`/category/${id}`, {
            method: 'DELETE'  // ✓ 使用DELETE方法
        })
        // ... existing code ...
    }
}