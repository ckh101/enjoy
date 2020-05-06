/* ------------------------------------------------------------------------------
*
*  # Stepy wizard
*
*  Specific JS code additions for wizard_stepy.html page
*
*  Version: 1.0
*  Latest update: Aug 1, 2015
*
* ---------------------------------------------------------------------------- */

$(function() {


    // Override defaults
    $.fn.stepy.defaults.legend = false;
    $.fn.stepy.defaults.transition = 'fade';
    $.fn.stepy.defaults.duration = 150;
    $.fn.stepy.defaults.backLabel = '<i class="icon-arrow-left13 position-left"></i> Back';
    $.fn.stepy.defaults.nextLabel = 'Next <i class="icon-arrow-right14 position-right"></i>';



    // Wizard examples
    // ------------------------------

    // Basic wizard setup
    $(".stepy-basic").stepy();


    // Hide step description
    $(".stepy-no-description").stepy({
        description: false
    });


    // Clickable titles
    $(".stepy-clickable").stepy({
        titleClick: true
    });


    // Stepy callbacks
    $(".stepy-callbacks").stepy({
        titleClick: true,
        nextLabel:"下一步<i class=\"icon-arrow-right14 position-right\"></i>",
        backLabel:"<i class=\"icon-arrow-left13 position-left\"></i>上一步",
        validate:true,
        legend:false,
        block: true,
        transition:'fade',
        duration:150,
        next: function(index) {
            alert('Going to step: ' + index);
            if (!$(".stepy-callbacks").validate(validate)) {
                return false
            }
        },
        back: function(index) {
            alert('Returning to step: ' + index);
        },
        finish: function() {
            alert('Submit canceled.');
            return false;
        }
    });


    // Initialize validation
    var validate = {
        ignore: 'input[type=hidden], .select2-input',
        errorClass: 'validation-error-label',
        successClass: 'validation-valid-label',
        highlight: function(element, errorClass) {
            $(element).removeClass(errorClass);
        },
        unhighlight: function(element, errorClass) {
            $(element).removeClass(errorClass);
        },
        errorPlacement: function(error, element) {
            if (element.parents('div').hasClass("checker") || element.parents('div').hasClass("choice") || element.parent().hasClass('bootstrap-switch-container') ) {
                if(element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
                    error.appendTo( element.parent().parent().parent().parent() );
                }
                 else {
                    error.appendTo( element.parent().parent().parent().parent().parent() );
                }
            }
            else if (element.parents('div').hasClass('checkbox') || element.parents('div').hasClass('radio')) {
                error.appendTo( element.parent().parent().parent() );
            }
            else if (element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
                error.appendTo( element.parent().parent() );
            }
            else if (element.parent().hasClass('uploader') || element.parents().hasClass('input-group')) {
                error.appendTo( element.parent().parent() );
            }
            else {
                error.insertAfter(element);
            }
        },
        rules: {
            email: {
                email: true
            }
        }
    }



    // Initialize plugins
    // ------------------------------

    // Apply "Back" and "Next" button styling
    $('.stepy-step').find('.button-next').addClass('btn btn-primary');
    $('.stepy-step').find('.button-back').addClass('btn btn-default');

});
