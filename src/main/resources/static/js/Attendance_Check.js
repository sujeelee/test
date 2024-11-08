function Attendance_Check() {
    const id = "${#authentication.Principal.member.mb_id}";
    alert(1);
}

/*
$.ajax({
    url: "/event/ajax/Attendance_Check",
    method: "post",
    dataType: "text",
    data: { id: id },
    success: function (data) {
      alert("아이디 전송 완료");
      if (data) {
        alert("출석체크 완료");
      } else {
        alert("이미 출석체크 하셨습니다");
      }
    },
    error: function (data, status, err) {
      alert("로그인 후 이용해 주세요");
      console.log(data);
    },
  });
  */
