var ajaxUrl = 'ajax/meals';
var datatableApiMeal;

$(function () {
    datatableApiMeal = $('#datatableMeals').DataTable({
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
    // makeEditable();
});
