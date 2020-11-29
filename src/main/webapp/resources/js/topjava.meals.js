var mealCtx, ajaxMealUrl;

ajaxMealUrl = "profile/meals/"

function updateMealTable() {
    $.get(ajaxMealUrl, function (data) {
        mealCtx.datatableMealApi.clear().rows.add(data).draw();
    });
}

function saveMeal() {
    $.ajax({
        type: "POST",
        url: ajaxMealUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateMealTable();
        successNoty("Saved");
    });
}

function deleteRow(id) {
    $.ajax({
        url: ajaxMealUrl + id,
        type: "DELETE"
    }).done(function () {
        updateTable();
        successNoty("Deleted");
    });
}

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: ajaxMealUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateMealTable);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxMealUrl, updateMealTable);
}

$(function () {

    mealCtx = {
        ajaxMealUrl,
        datatableMealApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
        })
    };
    makeEditable();


//  http://xdsoft.net/jqplugins/datetimepicker/
    var startDate = $('#startDate');
    var endDate = $('#endDate');
    startDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d',
        onShow: function () {
            this.setOptions({
                maxDate: endDate.val() ? endDate.val() : false
            })
        }
    });
    endDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d',
        onShow: function () {
            this.setOptions({
                minDate: startDate.val() ? startDate.val() : false
            })
        }
    });

    var startTime = $('#startTime');
    var endTime = $('#endTime');
    startTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function () {
            this.setOptions({
                maxTime: endTime.val() ? endTime.val() : false
            })
        }
    });
    endTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function () {
            this.setOptions({
                minTime: startTime.val() ? startTime.val() : false
            })
        }
    });
    $.datetimepicker.setLocale(localeCode);
    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });
});