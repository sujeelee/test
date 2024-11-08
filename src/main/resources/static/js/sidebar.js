function showDash(icon) {
	
	var tag  = $(icon).data("tag"); //이걸로 대시인포에 보여줄 내용 컨트롤
	if(tag == "hot"){
		$(".dash-info").find("div").hide();
		$(".dash-hot-stocks").show();
		$(".dash-hot-stocks").find("div").show();
	} else {
		$(".dash-info").find("div").show();
		$(".dash-hot-stocks").hide();
		$(".member-dash-title").text($(icon).find(".links_name").text());
		get_list_dash(tag);
	}
    if (!$(icon).hasClass("active")) {
        // 사이드바 열기
        $(".dash-info").css("display", "block");
        
        // 패딩 값 늘리기
        $(".navbar").css("padding", "15px 80px"); // 패딩을 15px 40px로 변경
        
        setTimeout(function() {
			$(".dash-info").css("width", "300px"); 
            $(".home-section").css("left", "-100px"); // home 요소를 오른쪽으로 밀기
        }, 50); // 자연스러운 애니메이션을 위한 짧은 지연

        $(".side_icon").removeClass("active");
        $(icon).addClass("active");
    } else {
        // 사이드바 닫기
        $(".dash-info").css("width", "0px"); // 사이드바를 닫기
        
        // 애니메이션 완료 후 home 요소 복원
        setTimeout(function() {
            $(".home-section").css("left", "0px"); // home 요소 원래 위치로 복원
            $(".dash-info").css("display", "none"); // 애니메이션 완료 후 사이드바 숨김
            
            // 원래 패딩 값으로 복원
           $(".navbar").css("padding", "15px 20px"); // 패딩을 원래대로 복원
        }, 250); // 사이드바 애니메이션과 일치시키기 위한 지연

        $(".side_icon").removeClass("active");
        $(icon).removeClass("active");
    }
}
