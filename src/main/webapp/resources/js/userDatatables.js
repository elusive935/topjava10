var ajaxUrl = 'ajax/admin/users';
var dataTableApi;

// $(document).ready(function () {
$(function () {
    dataTableApi = $('#usersDataTable').DataTable({
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

function updateUser(){
    var act = $('#activation');
    $.ajax({
        type: "POST",
        url: ajaxUrl + "/activation",
        data: act.serialize(),
        success: updateTable()
    });
}