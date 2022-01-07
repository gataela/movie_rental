    $(document).ready(function(){
    $('.table .nBtn').on('click', function(event) {
    event.preventDefault();
    var href = $(this).attr('href');
    var text = $(this).text();
    //for creating user
    $('.myFormCreate #firstName').val('');
    $('.myFormCreate #lastName').val('');
    $('.myFormCreate #email').val('');
    $('.myFormCreate #myModalCreate').modal();
    }
    });