$(function () {
    $('select#accountType').change(function (event) {
        if (event.target.selectedOptions[0].value == 'producer')
            $('#website-container').removeClass('d-none');
        else
            $('#website-container').addClass('d-none');
    });
});