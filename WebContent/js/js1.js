/**
 * 
 */

function getAllData(id) {
	$.ajax({
        url: 'myServletPath',
        type: 'POST',
        dataType: 'xml',
        data: {aa:'k', bb:'pp'},
        success: function(xml) {                  
            $("#ww").html($(xml).find('from').text());
        },
        error: function() {
            //alert("All bad!");
        }
    });
}
