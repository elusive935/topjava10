var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize(),
        success: updateTableByData
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    $('#startDate, #endDate').datetimepicker({timepicker : false, format:'Y-m-d'});
    $('#startTime, #endTime').datetimepicker({datepicker : false, format:'H:i'});
    $('#dateTime').datetimepicker({format:'Y-m-d H:i'});
    datatableApi = $("#datatable").DataTable({
        "ajax":{
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (data, type, row) {
                    if (type == 'display') {
                        return '<span>' + data.substring(0, 10) + ' ' + data.substring(11, 16) + '</span>';
                    }
                    return data;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "orderable": false,
                "defaultContent": "",
                "render": renderEditBtn
            },
            {
                "orderable": false,
                "defaultContent": "",
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            data.exceed ? $(row).addClass("exceeded"):
                $(row).addClass("normal");
        },
        "initComplete": makeEditable
    });
});