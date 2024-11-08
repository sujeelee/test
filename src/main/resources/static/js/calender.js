$(document).ready(function () {
	var months = [];
    var dateObj = new Date();
    var selectYear = dateObj.getFullYear();
    var disableYear = dateObj.getFullYear();
    var month = dateObj.getMonth() + 1;
    var j = 0 ;
    
    // 해당하지 않는 월 disable
    for ( var i = month + 1 ; i <=12; i ++){
        months[j++] = i;
    }
    
	var options = {
			pattern: 'yyyy-mm'       // input태그에 표시될 형식
			,selectedYear: selectYear       // 선택할 연도
			,startYear: 2020          // 시작연도
			,finalYear: disableYear          // 마지막연도
			,monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']         // 화면에 보여줄 월이름
			,openOnFocus: false       // focus시에 달력이 보일지 유무
    	};
    $("#calenders").monthpicker(options);
    if ( disableYear == selectYear ) {
        $("#calenders").monthpicker('disableMonths', months);
    }
    // 년도 바꿀 경우 월 able
    $('#calenders').monthpicker().on('monthpicker-change-year', function(e,year){
        if ( year == selectYear.toString() ) {
            $('#calenders').monthpicker('disableMonths', months);
        } else {
            $('#calenders').monthpicker('disableMonths', []);
        }
    });

	$('#calender-btn').on('click', function () {
	    $('#calenders').monthpicker('show');
	});
});
