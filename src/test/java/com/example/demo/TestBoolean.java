package com.example.demo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestBoolean {

	public static void main(String[] args) {
		RuntimeException re = new RuntimeException("에러 정상이 아니라는 거지..");
		log.debug("test boolean");
		
		// 소스 코드를 간결하게 작성해봤다.
		// 아래 소스중. isAllClear 는 위에서 부터 true 로 정상 처리됨을 가정한다.
		// 여기서 에러가 발생하면 false 로.
		// 최초 이미 에러가 있어서 false 면 항상 false 로. 이게 체크가 되는지 검토하기 위한 테스트 코드이다.
		
//		if (prepareMap != null) { // cpn_grp_issue_hst s3backup & target delete.
//			isAllClear = this.deleteCpnGrpIssueHst(prepareMap, resultMap) // 쿠폰그룹 지급이력 삭제처리 및 여부. 
//					&& isAllClear; // 모두 정상 처리 값 설정.
//		}
		
		boolean isAllClear = true;
		isAllClear = getReturn(false) && isAllClear; // false
		if ( isAllClear != false ) throw re;
		
		
		isAllClear = true;
		isAllClear = getReturn(true) && isAllClear; // true
		if ( isAllClear != true ) throw re;
		
		
		isAllClear = false;
		isAllClear = getReturn(false) && isAllClear; // false
		if ( isAllClear != false ) throw re;
		
		
		isAllClear = false;
		isAllClear = getReturn(true) && isAllClear; // false
		if ( isAllClear != false ) throw re;
		
		log.error("여기까지 오면 정상이란다.");
		
	}
	
	private static boolean getReturn(boolean is) {
		log.debug("여기는 무조건 실행! OK? 그래야 전체 실행을 하니까...");
		return is;
	}

}
