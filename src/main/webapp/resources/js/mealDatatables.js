var ajaxUrl = 'ajax/meals';
var dataTableApi;

$(function () {
    $('#startDate').datetimepicker({timepicker : false, format:'Y-m-d'});
    $('#startTime').datetimepicker({datepicker : false, format:'H:i'});
    $('#endDate').datetimepicker({timepicker : false, format:'Y-m-d'});
    $('#endTime').datetimepicker({datepicker : false, format:'H:i'});
    $('#dateTime').datetimepicker({format:'Y-m-d H:i'});
    dataTableApi = $('#datatable').DataTable({
        "paging":false,
        "info":true,
        "columns":[
            {"data":"dateTime"},
            {"data":"description"},
            {"data":"calories"},
            {"defaultContent":"Edit",
                "orderable":false},
            {"defaultContent":"Delete",
                "orderable":false}],
        "order":[
            [0, "desc"]
        ]
    });
    makeEditable();
});

function clearFilter(){
    $('#filter')[0].reset();
    updateTable();
}

function updateTableWithFilter() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "/filter",
        data: $("#filter").serialize(),
        success: fillTable
    })
}
