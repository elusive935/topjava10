var ajaxUrl = 'ajax/admin/users';
var dataTableApi;

// $(document).ready(function () {
$(function () {
    dataTableApi = $('#datatable').DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});

function updateUser(userId, checkbox){
    $.ajax({
        type: "POST",
        url: ajaxUrl + "/activation",
        data: {userId:userId, enabled:checkbox.is(':checked')},
        success: updateTable()
    });
}