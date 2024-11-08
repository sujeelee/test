function validateForm() {
	const titlePattern = /^[0-9가-힣a-zA-Z\s]+$/;
	const telFaxPattern = /^\d{2,3}-\d{3,4}-\d{4}$/;
	const emailPattern = /^[\w-]+@([\w-]+\.)+[\w-]{2,4}$/;
	const numberPattern = /^\d+$/;

	const title = document.getElementById("cf_title").value;
	const tel = document.getElementById("cf_tel").value;
	const fax = document.getElementById("cf_fax").value;
	const email = document.getElementById("cf_email").value;
	const dayPoint = document.getElementById("cf_day_point").value;
	const odPoint = document.getElementById("cf_od_point").value;
	const percent = document.getElementById("cf_percent").value;

	if (!titlePattern.test(title)) {
		alert("홈페이지 제목이 부적절 합니다.");
		return false;
	}
	if (!telFaxPattern.test(tel)) {
		alert("올바른 전화번호가 아닙니다.");
		return false;
	}
	if (!telFaxPattern.test(fax)) {
		alert("올바른 팩스 번호가 아닙니다.");
		return false;
	}
	if (!emailPattern.test(email)) {
		alert("올바른 이메일 형식이 아닙니다.");
		return false;
	}
	if (!numberPattern.test(dayPoint)) {
		alert("포인트는 숫자만 입력 가능합니다.");
		return false;
	}
	if (!numberPattern.test(odPoint)) {
		alert("포인트 지급률은 숫자만 입력 가능합니다.");
		return false;
	}
	if (!numberPattern.test(percent)) {
		alert("구매 수수료는 숫자만 입력 가능합니다.");
		return false;
	}

	return true;
}
