package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import skt.mno.mpai.mps.global.util.ObjectUtil;
import skt.mno.mpai.mps.global.util.StringUtil;


@Slf4j
public class TestMemberServiceController {
	public static void main(String[] args) {
		log.info("\nerrorTest start");
		errorTest(); // 에러 원본.
		log.info("\neditedTest start");
		editedTest(); // 에러 수정본.
		log.info("\ntest end\n");
	}
	
	public static void errorTest() {
		HashMap<String,Object> memberInfo = new HashMap<String,Object>() ;
    	List<HashMap<String,Object>> lst = new ArrayList<>();
    	List<HashMap<String,Object>> lst2 = new ArrayList<>();
		List<HashMap<String,Object>> svcNumList = new ArrayList<>();
		
		// 데이터 2건 조회됨.
		/* 소스 파악.
		listResult = delegator.callIcasServiceList("GetMultiMbrCardSV", parameters, IcasTypes.GET_ICAS);
    	for(int i = 0 ; i < listResult.size() ; i++) {
			svcNumList.add(mapIndex , multiMemberInfo);
    	}
		*/
		// test data in
		{
			HashMap<String, Object> multiMemberInfo1 = new HashMap<>();
			multiMemberInfo1.put("mbrSvcNum", "");
			multiMemberInfo1.put("mbrGrCd", "");
			svcNumList.add(multiMemberInfo1);
		}
		{
			HashMap<String, Object> multiMemberInfo1 = new HashMap<>();
			multiMemberInfo1.put("mbrSvcNum", "01071002000");
			multiMemberInfo1.put("mbrGrCd", "S");
			svcNumList.add(multiMemberInfo1);
		}

		// svcNumList.add(mapIndex , multiMemberInfo);
		
		// case : 세션 in.
		memberInfo.put("mbrSvcNum", "01085001900");
		memberInfo.put("mbrGrCd", "G");

		// 타사 LITE 회원이 아닐 경우
		if(!"L".equals(ObjectUtil.getDataFromMap(memberInfo, "mbrGrCd", ""))) {
			svcNumList.add(0, memberInfo);
		}
		
		String mbrSvcNum = "01012341234";
		

    	try {
			if(svcNumList.size() > 0 ) {
				log.debug("svcNumList mbrSvcNum = " + mbrSvcNum);
				//대표회선 
				lst2 = svcNumList.stream().filter(o -> o.get("mbrSvcNum").toString().equals(mbrSvcNum))
						.collect(Collectors.toList());
				
				lst = svcNumList.stream().filter(o -> !o.get("mbrSvcNum").toString().equals(mbrSvcNum))
						.collect(Collectors.toList());
				Collections.sort(lst, (c1, c2) -> {
					return Integer.compare(Integer.parseInt(c1.get("mbrSvcNum").toString()),Integer.parseInt(c2.get("mbrSvcNum").toString()));
				});
	
				lst2.addAll(lst);
			}
		}catch(Exception e) {

			log.error("callGetMultiMbrCardSV error={}", e.getMessage(), e);
			if(!"L".equals(ObjectUtil.getDataFromMap(memberInfo, "mbrGrCd", ""))) {
				svcNumList.add(0, memberInfo);
			}
			log.info("svcNumList={}", svcNumList.size());
			svcNumList.forEach(svcNum -> {
				log.info("svcNum={}", svcNum);
			});
		}
		
		log.info("lst2={}", lst2);

		// 다른 곳은 아래의 함수를 이용해서 조회하며, sort 처리가 없음.
		// List<ServiceInfo> serviceInfoList = memberApi.getMemberServiceList(getX_SESSION_KEY_MEMBER(), mbrChlId);
	}
	
	public static void editedTest() {
		HashMap<String,Object> memberInfo = new HashMap<String,Object>() ;
    	List<HashMap<String,Object>> lst = new ArrayList<>();
    	List<HashMap<String,Object>> lst2 = new ArrayList<>();
		List<HashMap<String,Object>> svcNumList = new ArrayList<>();
		
		// 데이터 2건 조회됨.
		/* 소스 파악.
		listResult = delegator.callIcasServiceList("GetMultiMbrCardSV", parameters, IcasTypes.GET_ICAS);
    	for(int i = 0 ; i < listResult.size() ; i++) {
			svcNumList.add(mapIndex , multiMemberInfo);
    	}
		*/
		// test data in
		{
			HashMap<String, Object> multiMemberInfo1 = new HashMap<>();
			multiMemberInfo1.put("mbrSvcNum", "");
			multiMemberInfo1.put("mbrGrCd", "");
			svcNumList.add(multiMemberInfo1);
		}
		{
			HashMap<String, Object> multiMemberInfo1 = new HashMap<>();
			multiMemberInfo1.put("mbrSvcNum", "01071002000");
			multiMemberInfo1.put("mbrGrCd", "S");
			svcNumList.add(multiMemberInfo1);
		}

		// svcNumList.add(mapIndex , multiMemberInfo);
		
		// case : 세션 in.
		memberInfo.put("mbrSvcNum", "01085001900");
		memberInfo.put("mbrGrCd", "G");

		// 타사 LITE 회원이 아닐 경우
		if(!"L".equals(ObjectUtil.getDataFromMap(memberInfo, "mbrGrCd", ""))) {
			svcNumList.add(0, memberInfo);
		}
		
		String mbrSvcNum = "01012341234";
		

    	try {
			if(svcNumList.size() > 0 ) {
				log.debug("svcNumList mbrSvcNum = " + mbrSvcNum);
				//대표회선 
				lst2 = svcNumList.stream().filter(o -> o.get("mbrSvcNum").toString().equals(mbrSvcNum))
						.collect(Collectors.toList());
				
				lst = svcNumList.stream().filter(o -> !o.get("mbrSvcNum").toString().equals(mbrSvcNum))
						.collect(Collectors.toList());
				Collections.sort(lst, (c1, c2) -> {
					int c1int = 0;
					int c2int = 0;
					if ( c1.get("mbrSvcNum") != null && !StringUtil.isBlank((String)c1.get("mbrSvcNum")) ) {
						c1int = Integer.parseInt(c1.get("mbrSvcNum").toString());
					}
					if ( c2.get("mbrSvcNum") != null && !StringUtil.isBlank((String)c2.get("mbrSvcNum")) ) {
						c2int = Integer.parseInt(c2.get("mbrSvcNum").toString());
					}
		            return Integer.compare(c1int,c2int);
				});
	
				lst2.addAll(lst);
			}
		}catch(Exception e) {

			log.error("callGetMultiMbrCardSV error={}", e.getMessage(), e);
			if(!"L".equals(ObjectUtil.getDataFromMap(memberInfo, "mbrGrCd", ""))) {
				svcNumList.add(0, memberInfo);
			}
			log.info("svcNumList={}", svcNumList.size());
			svcNumList.forEach(svcNum -> {
				log.info("svcNum={}", svcNum);
			});
			return;
		}
		
		log.info("lst2={}", lst2);
	}
}
