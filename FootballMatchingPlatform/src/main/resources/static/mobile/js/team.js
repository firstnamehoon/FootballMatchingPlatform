
//팀 유무 판단
const userId = $('#sessionId').text();
console.log(userId);
isTeam();

// 전체 팀 순위 클릭 시
$('#allTeamRank').on('click', function () {
  $(".selectBtn").removeClass('btnClick');
  $("#allTeamRank > button").addClass('btnClick');
  $("#searchBox").show();
  $("#filter-container").hide();
  $("#createTeamBox").removeClass("d-block");
  $("#createTeamBox").addClass("d-none");
  $("#btnValue").text(1);
  allTeamRank();
})

// 가입 가능 팀 클릭 시
$('#possJoin').on('click', function () {
  $(".selectBtn").removeClass('btnClick');
  $("#possJoin > button").addClass('btnClick');
  $("#createTeamBox").removeClass("d-none");	// 팀 생성하기 버튼
  $("#filter-container").show();
  $("#createTeamBox").addClass("d-block");
  $("#btnValue").text(2);
  possJoinTeam();
})

// 가입 신청 목록
$('#joinList').on('click', function(){
	$(".selectBtn").removeClass('btnClick');
  	$("#joinList > button").addClass('btnClick');
  	$("#filter-container").hide();
  	$("#createTeamBox").removeClass("d-block");
 	$("#createTeamBox").addClass("d-none");
  	joinList();
})

// 팀 생성하기 버튼 클릭 시
$('#addTeam').on('click',function(){
	
	// 가입신청리스트에 신청 있으면 안됨!!
	$.ajax({
		url: "/team/joinApply",
		type: "GET",
		dataType: "json",
		success: function(data) {
			res = data.result;
			if(res.length === 0){
				location.href="/teamCreate";
			}
			else{
				tostOn("가입 신청된 팀이 있습니다.");
			}
		},
		error: function() {
			alert("error");
		}
	});

})

// 내 팀 정보 클릭 시
$("#myTeamInfo").on('click', function () {
  $(".selectBtn").removeClass('btnClick');
  $("#myTeamInfo > button").addClass('btnClick');
  $("#searchBox").hide();
  myTeamInfo();
})

$("#search").on("click", function () {
	var no = $('#btnValue').text();
	var search = $("#search-keyword").val();

	if(no === "1"){
		if(!search){
			allTeamRank();
			return;
		}
	  fetch("/team/rank/"+search)
	    .then(response => response.json())
	    .then(data => {
	      let res = data.result;
	      let str = "";
	      for (var i in res) {
	        str += `<div class="content-container border border-2 rounded-4">
	              <div class="first-content-container">
	              <div class="ranking">`+ res[i].teamRank + `</div>
	              <div class="name">
	                 <span class="teamname">`+ res[i].teamName + `</span>
	              </div>
	             </div>
	              <div class="second-content-container">
	              <div class="gameCount">`+ res[i].gameCount + `판</div>
	              <div class="odds">`+ res[i].odds + `%</div>
	              <div class="tier">`+ res[i].teamTier + `</div>
	              <div class="score">`+ res[i].teamScore + `점</div>
	              </div>
	            </div>`;
	      }
	      $('#dataBox').html(str);

	    })		
	}
	else{
		if(!search){
			allTeamRank();
			return;
		}
	  fetch("/team/possJoin/"+search)
	    .then(response => response.json())
	    .then(data => {
	      let res = data.result;
		  let str = strRes(res);
		  
	      $('#dataBox').html(str);
	      joinBtnClick();
	      teamInfoShow();
	    })			
	}
});	

function teamInfoShow(){
	$('#teamInfoModal').on('show.bs.modal', function (event) {
		teamSeq = $(event.relatedTarget).data('teamseq');
		teamName = $(event.relatedTarget).data('teamname');
		teamScore = $(event.relatedTarget).data('teamscore');
		teamTier = $(event.relatedTarget).data('teamtier');
		teamRank = $(event.relatedTarget).data('teamrank');
		gameCount = $(event.relatedTarget).data('gamecount');
		winCount = $(event.relatedTarget).data('wincount');
		content = $(event.relatedTarget).data('content');
		odds = $(event.relatedTarget).data('odds');
		$("#teamTitleModal").text(teamName);
		$("#info-rank").text(teamRank + "위");
		$("#info-tier").text(teamTier);
		$("#info-score").text(teamScore+"점");
		$("#info-count").text(gameCount + "전 " +winCount + "승");
		$("#info-odds").text(odds+"%");
		$("#info-content").text(content);				 				 
	}); 
}


// 팀 유무 확인
function isTeam() {
  fetch("/team/isTeam")
    .then(response => response.json())
    .then(data => {
		console.log(data.result);
      if (data.result === " ") {
        $("#myTeamInfo").hide();
        $(".selectBtn").removeClass('btnClick');
        $("#allTeamRank > button").addClass('btnClick');
          $("#filter-container").hide();
        $("#searchBox").show();
        allTeamRank();
      }
      else {
        $("#possJoin").hide();
        $("#joinList").hide();
        $(".selectBtn").removeClass('btnClick');
        $("#filter-container").hide();
        $("#myTeamInfo > button").addClass('btnClick');
        $("#searchBox").hide();
        myTeamInfo();
      }

    })
}


/* =========== 팀 순위 목록 =========== */

function allTeamRank() {
  fetch("/team/rank")
    .then(response => response.json())
    .then(data => {
      let res = data.result;
      let str = "";
      for (var i in res) {
        str += `<div class="content-container border border-2 rounded-4">
              <div class="first-content-container">
              <div class="ranking">`+ res[i].teamRank + `</div>
              <div class="name">
                 <span class="teamname">`+ res[i].teamName + `</span>
              </div>
             </div>
              <div class="second-content-container">
              <div class="gameCount">`+ res[i].gameCount + `판</div>
              <div class="odds">`+ res[i].odds + `%</div>
              <div class="tier">`+ res[i].teamTier + `</div>
              <div class="score">`+ res[i].teamScore + `점</div>
              </div>
            </div>`;
      }
      $('#dataBox').html(str);

    })
}


/* =========== 가입 가능한 팀 목록 =========== */

function strRes(res){
	let str = "";
	for (var i in res) {
        str +=
          `<div class="content-container border border-2 rounded-4 p-3">
                <div class="teamInfo col-9 d-flex align-items-center content-text p-0"
                data-bs-toggle="modal" data-bs-target="#teamInfoModal"
                data-teamseq="`+res[i].teamSeq +`"
                data-teamname="`+ res[i].teamName+`"
                data-teamrank="`+ res[i].teamRank +`"
                data-teamscore="`+ res[i].teamScore +`"
                data-teamtier="`+ res[i].teamTier +`"
                data-gamecount="`+ res[i].gameCount +`"
                data-wincount="`+ res[i].winCount +`"
                data-odds="`+ res[i].odds +`"
                data-content="`+ res[i].content +`">
                  <div class="d-flex">
                      <div class="content-title pe-2">`+ res[i].teamName + `</div>
                      <div class="content-tier ps-1">`;

        if (res[i].possA == false && res[i].possB == false &&
          res[i].possC == false && res[i].possD == false) {
          str += '<span class="badge rounded-5">남녀무관</span>';
        }
        else {
          if (res[i].possA == true)
            str += '<span class="badge rounded-5">A</span>';
          if (res[i].possB == true)
            str += '<span class="badge rounded-5">B</span>';
          if (res[i].possC == true)
            str += '<span class="badge rounded-5">C</span>';
          if (res[i].possD == true)
            str += '<span class="badge rounded-5">D</span>';
        }
        str += `</div>
              </div>
              <div class="content-info">`+ res[i].weekType + `/` +
          res[i].weekTime + `/` + res[i].hopeTime + `/` + res[i].hometown + `</div>
              <div class="content-member p-0">` + res[i].memberCount + `/10명</div>
           </div>
           <div class="col d-flex justify-content-end p-0">
              <button class="joinBtn rounded-5 p-2"
              data-bs-toggle="modal" data-bs-target="#addApplyModal"
              data-teamno = "`+ res[i].teamSeq+`" 
              data-name="`+ res[i].teamName +`">가입신청</button>
            </div>
         </div>`;

      }
      
      return str;
}

//가입 가능팀 조회
function possJoinTeam() {
  fetch("/team/possJoin")
    .then(response => response.json())
    .then(data => {
      let res = data.result;
      str = strRes(res);   
      console.log(res);   
      $('#dataBox').html(str);
      
      teamInfoShow();      
      joinBtnClick();    

    })
}

// 가입 신청 버튼
function joinBtnClick() {
  
	$('#addApplyModal').on('show.bs.modal', function(event) {          
		teamName = $(event.relatedTarget).data('name');
		$('#teamModalTitle').text(teamName);
		
		var teamSeq = $(event.relatedTarget).data('teamno');
		
		$("#addTeamApply").on('click',function(){
			$.ajax({
		      url: "/team/applyTeam/"+teamSeq,
		      type: "Post",
		      dataType: "json",
		      success: function(data) {
		          res = data.result;
		          location.reload(true);
		      },
		      error: function() {
		          alert("error");
		      }
		    });
		})
		
	});
}


/* =========== 가입 신청 목록 =========== */

// 신청 취소 버튼
function cancelBtnClick(){
	
	$('#cancelModal').on('show.bs.modal', function(event) {          
		teamName = $(event.relatedTarget).data('name');
		$('#teamModalTitle2').text(teamName);
		var teamSeq = $(event.relatedTarget).data('teamno');
		
		$("#cancelApply").on('click',function(){
			$.ajax({
		      url: "/team/cancel/"+teamSeq,
		      type: "delete",
		      dataType: "json",
		      success: function(data) {
		          res = data.result;
		          location.reload(true);
		      },
		      error: function() {
		          alert("error");
		      }
		    });
		})
		
	});
}

function joinList(){
  fetch("/team/joinApply")
    .then(response => response.json())
    .then(data => {
      let res = data.result;

      let str = "";
      for (var i in res) {
		str += `
		<div class="content-container border border-2 rounded-4 p-3">
          <div class="teamInfo col-9 d-flex align-items-center content-text p-0">
            <div class="d-flex">
              <div class="content-title pe-2">`+ res[i].teamName +`</div>
              <div class="content-tier ps-1">`;

        if (res[i].possA == false && res[i].possB == false &&
          res[i].possC == false && res[i].possD == false) {
          str += '<span class="badge rounded-5">남녀무관</span>';
        }
        else {
          if (res[i].possA == true)
            str += '<span class="badge rounded-5">A</span>';
          if (res[i].possB == true)
            str += '<span class="badge rounded-5">B</span>';
          if (res[i].possC == true)
            str += '<span class="badge rounded-5">C</span>';
          if (res[i].possD == true)
            str += '<span class="badge rounded-5">D</span>';
        }
        
        str += `</div>
              </div>
              <div class="content-info">`+ res[i].weekType + `/` +
          res[i].weekTime + `/` + res[i].hopeTime + `/` + res[i].hometown + `</div>
              <div class="content-member p-0">` + res[i].memberCount + `/10명</div>
           </div>

          <div class="col d-flex justify-content-end p-0">
            <button class="cancelApply rounded-5 p-2"
             data-bs-toggle="modal" data-bs-target="#cancelModal"
              data-teamno = "`+ res[i].teamSeq+`" 
              data-name="`+ res[i].teamName +`">신청취소</button>
          </div>
        </div>
		`;
      }
      $('#dataBox').html(str);
	  cancelBtnClick();
    })	
    
}


// ====================== 팀 있을 경우 ======================

// 팀장 조회
function getTeamLeader(teamSeq){
	let res = {};
	$.ajax({
	    url: "/team/leader/"+teamSeq,
	    type: "GET",
	    async: false,
	    dataType: "json",
	    success: function(data) {
	        res = data.result;
	    },
	    error: function() {
	        alert("error");
	    }
  	});

	return res;
}



// [ 버튼 이벤트 ]
// 팀 나가기
function updateMemberStatus() {
	$('#updateMemberStatus').on('click',function(){
		  $.ajax({
		    url: "/team/updateStatus",
		    type: "PUT",
		    dataType: "json",
		    success: function(data) {
		        res = data.result;
		        location.reload(true);
		    },
		    error: function() {
		        alert("error");
		    }
		  });
	})
}



// 팀원 조회
function myTeamMember(teamSeq){
  let res = {};
  $.ajax({
    url: "/team/myTeamMember/"+teamSeq,
    type: "GET",
    async: false,
    dataType: "json",
    success: function(data) {
        res = data.result;
    },
    error: function() {
        alert("error");
    }
  });

  return res;
}

// 다음 경기 일정
function myTeamSchedule(teamSeq){
  let res;
  $.ajax({
    url: "/team/teamSchedule/"+teamSeq,
    type: "GET",
    async: false,
    dataType: "json",
    success: function(data) {
        res = data.result;
    },
    error: function() {
        alert("error");
    }
  });

  return res;
}


// [더보기] 팀원 정보
function moreTeamMember(){
	$('#moreTeamMember').on('click',function(){
		location.replace('/teamMember');
	})
}
// 내 팀정보
function myTeamInfo() {
  fetch("/team/myTeam")
    .then(response => response.json())
    .then(data => {
      let res = data.result;
      $("#searchBox").hide();
	 	
      const members = myTeamMember(res.teamSeq);
      const leader = getTeamLeader(res.teamSeq);
      let schedule = myTeamSchedule(res.teamSeq);
      console.log(schedule)
		if(schedule.matchingStatus === null)
			schedule = null;
      let str = "";

      str += `
        <!-- 팀 정보 -->
                <div
                    class="d-flex gap-2 w-100 align-items-center justify-content-center border border-2 rounded-4 p-2 pt-4 pb-4 mt-2">
                    <div class="d-flex text-center">
                        <div class="team-title pe-3">`+
                            res.teamName
                        +`</div>
                    </div>
                    <div class="d-flex flex-column gap-1">
                        <div class="">
                            `+ res.teamRank +"위 "+ res.teamTier+"등급"+
                            `<span class="team-score">[`+res.teamScore+`점]</span>
                        </div>
                        <div class="">`+res.gameCount+"전 "+ res.winCount+`승
                            <span class="team-score">(승률 `+ res.odds+`%)</span>
                        </div>
                        <span class="badge rounded-5 w-50">`+ res.memberCount +` / 10 명</span>
                    </div>
                </div>

                <!-- 일정 -->
                <div class="d-flex w-100 ps-2 mt-2">
                    <div>다음 일정</div>
                </div>
                <div class="d-flex justify-content-between w-100 border border-2 rounded-4 p-3">`
                    
                    if(schedule === null){
						str += `<div class="p-3 text-center">경기 일정이 없습니다.</div>`
					}else{
				str +=                     `<div>
                        <div class="d-flex">
                            <div class="pe-1">`+res.teamName+`</div>
                            <div>vs</div>
                            <div class="ps-1">`+schedule.rival+`</div>
                        </div>
                        <div class="d-flex gap-2">
                            <div>`+ schedule.matchingDate +`</div>
                            <div>`+ schedule.matchingTime+`:00~`+(schedule.matchingTime+2)+
                            `:00` +`</div>
                        </div>
                        <div class="d-flex">
                            <div>`+schedule.fieldName+`</div>
                        </div>
                    </div>
                    <div class="d-flex justify-content-end team-img">
                        <img src="/mobile/img/구장 사진.svg">
                    </div>`
						
					}

                
             str += `</div>

                <!-- 팀원 -->
                <div class="d-flex justify-content-between w-100 ps-2 mt-2">
                    <div>팀원</div>
                   
                    <!-- 팀장일 경우에만 -->`;                
                    if(leader === userId){
						str += `<div><a href="/teamMember?teamSeq=`+res.teamSeq+`" class="link-secondary pe-2 id='moreTeamMember'">더보기</a></div>`;
					}               
                    
              str += `</div>
                <div class="d-flex w-100 border border-2 rounded-4 p-3 justify-content-center flex-wrap gap-3"
                id="memberList">
                    `;
                   
                    for(var i in members){
                      str += `
                            <div class="member_list_box">
                              <div class="d-flex nickname_container">
                                <div>`+ members[i].nickname +`</div>`
                      if(i == 0){
                        str += ` <img src="/mobile/img/leader.svg" class="teamKing">`;
                      }
                      else if(members[i].newbie){
                        str += `<div class="nickBadge">new</div>`;
                      }
                      str +=    `</div>
                              <div>`+ members[i].userTier+`</div>
                            </div>
                      `;
                    }

                    
                   str += `
                </div>

                <div class="d-flex w-100 ps-2 mt-2">
                    <div>팀 설명</div>
                </div>
                <div class="w-100 border border-2 rounded-4 p-3 mb-2">
                    <div class="d-flex align-items-center gap-1">
                        <div>신청 가능 등급 : </div>`
                        if (res.possA == false && res.possB == false &&
                          res.possC == false && res.possD == false) {
                          str += '<span class="badge rounded-5">남녀무관</span>';
                        }
                        else {
                          if (res.possA == true)
                            str += '<span class="badge rounded-5">A</span>';
                          if (res.possB == true)
                            str += '<span class="badge rounded-5">B</span>';
                          if (res.possC == true)
                            str += '<span class="badge rounded-5">C</span>';
                          if (res.possD == true)
                            str += '<span class="badge rounded-5">D</span>';
                        }
                  str+=`</div>
                    <div class="d-flex gap-1">
                        <div>시간 :</div>
                        <div>`+ res.weekType +`</div>
                        <div>`+ res.weekTime +`</div>
                        <div>`+ res.hopeTime+`</div>
                    </div>
                    <div class="d-flex gap-1">
                        <div>장소 :</div>
                        <div>`+ res.hometown +`</div>
                    </div>
                    <div class="d-flex flex-wrap">
                        <div>
                            `+ res.content +`
                        </div>
                    </div>
                </div>`

			if(leader === userId){
				str += `<div class="d-flex gap-2 justify-content-center mb-2">`;
				
				if(!res.recruitment){
              		str +=`<div class="">
                        		<button type="button" class="joinBtn p-2 ps-4 pe-4" id="addMember">팀원 추가 모집</button>
                  			</div>`;					
				}
				else{
              		str +=`<div class="">
                        		<button type="button" class="joinBtn p-2 ps-4 pe-4" id="joinApplyList">신청 목록</button>
                  			</div>`;						
				}
	
              	str +=`<div class="">
                        	<button type="button" class="joinBtn p-2 ps-4 pe-4" id="deleteTeamBtn"
                        	 data-bs-toggle="modal" data-bs-target="#deleteTeamModal"
                        	>팀 해체하기</button>
                   		</div>
                	</div>`;
			}	
			else{
		str += `<div class="d-flex gap-2 justify-content-center mb-2">
                    <div class="">
                        <button type="button" class="joinBtn p-2 ps-4 pe-4" id="updateMemberStatusBtn"
                        data-bs-toggle="modal" data-bs-target="#setStatus">팀 나가기</button>
                    </div>
                </div>`;
			}
             
      	$('#dataBox').html(str);
		updateMemberStatus();
		moreTeamMember();
		getJoinApplyListAndDeleteTeam(res.memberCount, res.teamSeq);
    })
}

// 신청 목록 & 팀 해체하기
function getJoinApplyListAndDeleteTeam(memberCount, teamSeq){
	// 신청목록
	$('#joinApplyList').on('click', function(){
		location.href="/applyList?teamSeq="+teamSeq;
	})
	
	//추가모집하기
	$('#addMember').on('click',function(){
		location.href="/teamCreate?teamSeq="+teamSeq;
	})
	
	//팀 해체하기
	$('#deleteTeam').on('click', function(){
		if(memberCount !== 1){	
			tostOn("팀원이 존재합니다.");
		}else{
			$("#tost_message").text("팀 해체 완료");		  
		  $.ajax({
		    url: "/team/dismantle/"+teamSeq,
		    type: "PUT",
		    dataType: "json",
		    success: function(data) {
		        res = data.result;
		        location.reload(true);
		    },
		    error: function() {
		        alert("error");
		    }
		  });			
		}	
	})
	
}

function tostOn(str){
	$("#tost_message").addClass("active");
	$("#tost_message").text(str);
    setTimeout(function(){
		$("#tost_message").removeClass("active");
    },2000);
}


