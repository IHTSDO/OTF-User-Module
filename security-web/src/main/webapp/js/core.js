var Splendid = Splendid || {};

$(function(){
        $('#mySlideContent1').css('display','none');
        $('#mySlideToggler1').click(function(){
            $('#mySlideContent1').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent2').css('display','none');
        $('#mySlideToggler2').click(function(){
            $('#mySlideContent2').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent3').css('display','none');
        $('#mySlideToggler3').click(function(){
            $('#mySlideContent3').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent4').css('display','none');
        $('#mySlideToggler4').click(function(){
            $('#mySlideContent4').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent5').css('display','none');
        $('#mySlideToggler5').click(function(){
            $('#mySlideContent5').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent6').css('display','none');
        $('#mySlideToggler6').click(function(){
            $('#mySlideContent6').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent7').css('display','none');
        $('#mySlideToggler7').click(function(){
            $('#mySlideContent7').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent8').css('display','none');
        $('#mySlideToggler8').click(function(){
            $('#mySlideContent8').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent9').css('display','none');
        $('#mySlideToggler9').click(function(){
            $('#mySlideContent9').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent10').css('display','none');
        $('#mySlideToggler10').click(function(){
            $('#mySlideContent10').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent11').css('display','none');
        $('#mySlideToggler11').click(function(){
            $('#mySlideContent11').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent12').css('display','none');
        $('#mySlideToggler12').click(function(){
            $('#mySlideContent12').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
        $('#mySlideContent13').css('display','none');
        $('#mySlideToggler13').click(function(){
            $('#mySlideContent13').slideToggle('slow');
            $(this).toggleClass('slideSign');
            return false;
        });
    });


Splendid.populateElement = function (selector, defvalue) {
    // Use: splendid.populateElement('#MYID', 'Lorum ipsum');
    if ($.trim($(selector).val()) == '') {
        $(selector).val(defvalue);
    }

    $(selector).focus(function () {
        if ($(selector).val() == defvalue) {
            $(selector).val("");
        }
    });

    $(selector).blur(function () {
        if ($.trim($(selector).val()) == '') {
            $(selector).val(defvalue);
        }
    });
};