/**
 * 
 */
$(function() {
	$(".tab_item").not(":first").hide();
	$(".tab_wrapper").find(".tab").on("click", function() {
	$(".tab_wrapper").find(".tab").removeClass("active").eq($(this).index()).addClass("active");
	$(".tab_item").hide().eq($(this).index()).fadeIn();
	}).eq(0).addClass("active");
});

