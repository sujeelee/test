function validateForm() {
	const idPattern = /^[a-zA-Z0-9]{4,20}$/;
	const telPattern = /^\d{3}-\d{3,4}-\d{4}$/;
	const emailPattern = /^[\w-]+@([\w-]+\.)+[\w-]{2,4}$/;
	const birthPattern = /^\d{4}-\d{2}-\d{2}$/;
	const numberPattern = /^\d+$/;
	const levelPattern = /^(10|[1-9])$/;

	const id = document.getElementById("mb_id").value;
	const tel = document.getElementById("mb_ph").value;
	const email = document.getElementById("mb_email").value;
	const birth = document.getElementById("mb_birth").value;
	const point = document.getElementById("mb_point").value;
	const level = document.getElementById("lvl").value;
	console.log(level)
	if (!idPattern.test(id)) {
		alert("아이디는 영문 또는 숫자로 4~20자 입력 가능합니다.");
		return false;
	}
	if (!numberPattern.test(point)) {
		alert("포인트는 숫자만 입력 가능합니다.");
		return false;
	}
	if (!telPattern.test(tel)) {
		alert("전화번호 형식이 올바르지 않습니다.");
		return false;
	}
	if (!emailPattern.test(email)) {
		alert("이메일 형식이 올바르지 않습니다.");
		return false;
	}
	if (!birthPattern.test(birth)) {
		alert("생년월일은 yyyy-mm-dd 형식으로 입력해주세요.");
		return false;
	}
	if (!levelPattern.test(level)) {
		alert("1~10까지만 설정 가능합니다.");
		return false;
	}

	return true;
}