$(document).ready(function () {
	var imgWidth = 450;
	var pos = -500; 

    function slide() {
		$("li").animate({ "left": "+=5px" }, 100, again);
	}

    $("li").each(function () {
		pos += imgWidth;
		$(this).css("left", pos);
	});

    function checkPos() {
        $("li").each(function () { 
            var left = $(this).parent().offset().left + $(this).offset().left;
            if (left >= 1800) {
                $(this).remove();
            }
        });
    }

    function again() {
		var left = $(this).parent().offset().left + $(this).offset().left;
		if (left >= 1300) {
            var liClone = $(this).clone();
            liClone.insertAfter(this);
            $(this).css("left", left - 1800);
            checkPos();
		}
		slide();
	}

    function stopSlider() {
        $("li").each(function () {
			$(this).stop(true);
		});
    }


    $("li").click(function () {
        showImage(this);
    })

    function showImage(object) {
        stopSlider();
        var images = $(object).find("img");
        var image = images.first();
        var cloned = image.clone();
        cloned.appendTo('body');
        cloned.addClass('bigImage');
        $(cloned).click(function () {
            slide();
            cloned.remove();
        })
    }

    slide();
});