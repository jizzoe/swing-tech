function onBlur(el) {
    if (el.value == '') {
        el.value = el.defaultValue;
    }
}
function onFocus(el) {
    if (el.value == el.defaultValue) {
        el.value = '';
    }
}


jQuery(document).ready(function() {
  jQuery(".moreButton").click(function()
  {
  	// get the index value for this button -- the associated content area
  	// has the equivalent index value
  	var elemIndex = $(".moreButton").index(this);
	jQuery(".extraContent").eq(elemIndex).slideToggle('slow');
	jQuery(this).fadeToggle('slow');
	
	return false;
  });
  
  jQuery(".closeButton").click(function()
  {
  	var elemIndex = $(".closeButton").index(this);
	jQuery(".extraContent").eq(elemIndex).slideToggle('slow');
	jQuery(".moreButton").eq(elemIndex).fadeIn('slow');
	
	return false;
  });
});

/**
 * Custom code for Catch.
 *
 * @author allemana
 */
jQuery(document).ready(function() {
	// setup carousel code
	if ($(".main-carousel").length) {
		// generic function to vertically center the visible slide
		$.fn.catchCenterSlide = function() {
			return this.each(function() {
				var $slide    = $(this).hasClass('item') ? $(this) : $('.main-carousel-item .item.active');
				var $slideImg = $slide.find('img');

				$slideImg.css('margin-top',
					((Math.floor(($slide.height()
						-$slideImg.height())/2))+'px'));
			});
		};
		
		// center first slide after image loads
		$('.main-carousel-item .item.active img').load(function() {
			setTimeout(function() { $(document).catchCenterSlide(); }, 0);
		});
		
		// place slider arrow
		var $selIcon = $("[rel='carousel-icon'][selected=selected]");
		var $slideArrow = $(".sliding-arrow[rel='"+$selIcon.attr('class')+"']");
		
		// set a function in the slide arrow to get its x position
		// relative to an element ($item)
		$slideArrow.getXPos = function($item) {
			var xpos = Math.ceil($item.offset().left
						-$item.parent().offset().left
						+($item.width()/2)
						-Math.ceil($slideArrow.width()/2));
						
			return xpos;
		}
		
		if ($selIcon.length && $slideArrow.length) {
			$slideArrow.css('left', $slideArrow.getXPos($selIcon)+'px');
		}
		
		$("[rel='carousel-icon']").each(function() {
			// set hover/out states for icons
			$(this).hover(function() {
				if (!$(this).attr("selected")) {
					$(this).css('opacity', 1);
				}
			}, function() {
				if (!$(this).attr("selected")) {
					$(this).css('opacity', 0.5);
				}
			});
			
			// set click actions
			$(this).click(function(e) {
				if (!$(this).attr('selected')) {
					// get currently selected item index
					var oldItemIndex = $("[rel='carousel-icon'][selected=selected]").index("[rel='carousel-icon']");
					var newItemIndex = $(this).index("[rel='carousel-icon']");
				
					$(this).siblings("[rel='carousel-icon']").css('opacity', 0.5).removeAttr("selected");
					$(this).css('opacity', 1).attr("selected", "selected");
				
					// fade out current slide, fade in selected
					$(".main-carousel-item").eq(oldItemIndex).fadeOut(500);
					$(".main-carousel-item").eq(newItemIndex).fadeIn(500);
					
					// move arrow						
					$slideArrow.animate({left: $slideArrow.getXPos($(this))}, 500);
				}
				
				return false;
			});
		})
		// dim all icons except selected
		.not("[selected=selected]").css('opacity', 0.5);
		
		// hide main carousel items not related to selected icon;
		// items are compared by index value, so the main carousel
		// blocks must be in the same order as the icons
		$(".main-carousel-item").not(function() {
			return $(this).index('.'+$(this).attr('class')) == $selIcon.index("[rel='carousel-icon']");
		}).css('display', 'none');
		
		// start slideshow on initial carousel
		//$("#catchCarousel1").carousel();
		
		// when the carousel finishes sliding a slide,
		// highlight the appropriate dot
		$(".carousel").on('slid', function() {
			$("[rel='"+$(this).attr('id')+"'] a")
			.eq($(this).find('.item.active').index())
			.addClass('selected')
			.siblings().removeClass('selected');
		});
	}
	
	// activate top button clicks
	$(".top-button-action").click(function(e) {
		var $popup = $(this).parent().find('.top-button-popup');
		
		if ($popup.css('display') == 'none') {
			// first create overlay
			if (!$('.catch-overlay').length) {
				$('body').prepend('<div class="catch-overlay"></div>');
			}
		
			// close all other popups
			$('.top-button-popup').not($(this)).fadeOut();
			
			// remove style from other tabs
			$('.top-button-action').removeClass('hilite');
			// set style
			$(this).addClass('hilite');
			
			// widen tab if we're not on home page :-|
			if (!$("body.catch-home #signupTop").length) {
				$('#signupForm .top-button-tab').css('width', '82px');
			}
			
			// open popup
			$popup.fadeIn(null, function() {
				// bind an event to listen for escape key,
				// which closes the popup
				$(document).one('keydown', function(e) {
					if (e.which == 27) {
						$('.top-button-action').removeClass('hilite');
						// close all popups
						$('.top-button-popup').fadeOut();
					}
				})
			});
			
			// give first text field focus
			$popup.find('input[type=text]').eq(0).focus();
		} else {
			// remove style
			$(this).removeClass('hilite');
			
			$popup.fadeOut();
		}
		
		return false;
	});
	
	// handle clicks on carousel "dots" (under the image)
	$(".carousel-dot").click(function(e) {
		var dotInd = $(this).index();
		
		// highlight this dot, unhighlight siblings
		$(this).siblings().removeClass('selected');
		$(this).addClass('selected');
		
		// rel attr of parent div must match id of the carousel it belongs to
		$('#'+$(this).parent().attr('rel')).carousel(dotInd);
		
		return false;
	});
	
	// utilize a generic class name for submitting forms via links
	$(".formSubmit").click(function(e) {
		e.preventDefault();
		$(this).closest("form").submit();
	});
	
	// special case for certain submit buttons
	$(".MenuElementSubmit a").click(function(e) {
		e.preventDefault();
		$(this).closest("form").submit();
	});
	
	// handle clicks on mobile options
	$(".mobileOptionHead").click(function(e) {
		$(this).find(".optionTitle").toggleClass("selected");
		$(this).closest(".mobileOption").find(".optionDesc").slideToggle();
	});
	
	// handle submission of the "get more info on premier package" form
	$("#premierInfoForm").submit(function(e) {
		return false;
	});
	
	$("#premierInfoSend").click(function(e) {
		$("#premierInfoError").slideUp();
		$form = $(this).closest("form");
		$.ajax({
			url: '/wp-content/plugins/catch_send_premier_info.php',
			type: 'POST',
			data: $form.serialize(),
			success: function(data) {
				if (data == "1") {
					$("#premierInfoForm").fadeOut(500, function() {
						$(this).html('<div id="premierInfoSuccess">Thank you! Your request has been received. We will contact you as soon as our premier package is available.</div>');
						$(this).fadeIn();
					});
				} else {
					$("#premierInfoError").html(data);
					$("#premierInfoError").slideDown();
				}
			}
		});
		return false;
	});
	
	// handle clicks of "read more" on press release page
	$(".prReadMore a").click(function(e) {
		$fullBody = $(this).closest(".prItem").find(".prFullBody");
		
		if ($fullBody.css("display") == 'none') {
			$(this).html('Read Less <img src="/wp-content/uploads/2011/10/btn_arrowUp.png" />');
		} else {
			$(this).html('Read More <img src="/wp-content/uploads/2011/09/btn_arrowDown.png" />');
		}
		
		$fullBody.slideToggle(1000);
		
		return false;
	});
	
	// handle hover over image links that specify hoverSrc
	$("img[hoverSrc]").hover(function(e) {
		var $img = $(this);
		var imgSrc = $img.attr('src');
		$img.attr('imgSrc', imgSrc);
		$img.attr('src', $img.attr('hoverSrc'));
	}, function(e) {
		var $img = $(this);
		if ($img.attr('imgSrc')) {
			$img.attr('src', $img.attr('imgSrc'));
		}
	});
	
	// set video thumbnails to use arrow on mouseover
	$("[rel$=arrow]").css('display', 'inline-block')
					.css('position', 'relative')
					//.append('<div class="video-hover"></div>')
					.hover(function(e) {
							$(this).find("img").css('opacity', '0.6');
							$(this).append('<div class="video-hover"></div>');
						},
						function(e) {
							$(this).find("img").css('opacity', 1);
							$(this).find('.video-hover').remove();
						}
					);
	
	// don't allow clicking of top menu items, also set cursor to default so
	// user doesn't get the idea they can click on it
	$("ul#menu-top a[href='#']").click(function(e) {
		e.preventDefault();
		return false;
	}).mouseover(function(e) {
		$(this).css('cursor', 'default');
	});
	
	// setup any photo/video galleries
	if ($("a[rel^='catchGallery']").length) {
		$("a[rel^='catchGallery']").prettyPhoto();
	}
	
	// give focus to log in button to simplify login process
	$("#loginTop").focus();
	
	// enable google event tracking; set rel attribute to the label value
	$("[eventTrack]").click(function(e) {
		var trackVals = $(this).attr("eventTrack").split(":");
		
		// only proceed with legal data
		if (trackVals.length >= 3 &&
			!isEmpty(trackVals[0]) && !isEmpty(trackVals[1]) && !isEmpty(trackVals[2])) {
			
			_gaq.push(['_trackEvent', $.trim(trackVals[0]), $.trim(trackVals[1]), $.trim(trackVals[2])]);
		}
	});
	
	// generic document click -- close any popups (with class 'catch-popup')
	$('.catch-overlay').live('click', function(e) {
		$('.top-button-action').removeClass('hilite');
		$('.catch-popup').fadeOut();
		$('.catch-overlay').remove();
	});
});


function isEmpty(val) {
	return /^\s*$/.test(val);
}
