package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.util.Base64Utils;

import lombok.extern.slf4j.Slf4j;
import skt.mno.mpai.mps.core.util.JacksonUtils;
import skt.mno.mpai.mps.global.dto.MpMemberDto;

@Slf4j
public class TestMain {
	/**
	 * 순수 자바 코드 테스트 실행.
	 * JUnit 테스트 코드는 나중에...
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String test  = null;
		System.out.print("11"+test);
		longRefTypeCompareError();
		testCreateHeaderMember();
		testCreateHeaderMemberModify();
	}
	
	private static void longRefTypeCompareError() {
		Long scmSubsProdGrpId = null;
		Long zero = 0L;
		if ( zero > 0L ) { // fake warning
			log.error("여기는 안되는 거...");
			scmSubsProdGrpId = 0L;
		}
		
		if ( scmSubsProdGrpId != null && scmSubsProdGrpId > 0L ) {
			log.info("흠 이게 안되는 거구나?");
		} else {
			log.debug("이렇게 1");
		}
		
		if ( zero < Optional.ofNullable(scmSubsProdGrpId).orElseGet(() -> 0L) ) {
			log.info("흠 이게 안되는 거구나?");
		} else {
			log.debug("이렇게 2");
		}
	}
	
	
	/**
	 * json 에서 가져온 정보를 member 로 치환하고, 정보를 수정해서 header 에 넣는 샘플.
	 */
	private static void testCreateHeaderMemberModify() {
		String sessionJson = getSessionJson();
		MpMemberDto member = JacksonUtils.toObjectNoEx(sessionJson, MpMemberDto.class);
		member.setMbrGrCd("O");
		// member.setMbrGrCd(null);
		// log.debug("member.getMpCardList()={}", member.getMpCardList());
		log.debug("testCreateHeaderMemberModify member={}", member);
		String browserHeader = Base64Utils.encodeToString(JacksonUtils.toJsonNoEx(member).getBytes(StandardCharsets.UTF_8));
		log.info("testCreateHeaderMemberModify :: browserHeader={}", browserHeader); // 이건 헤더에 넣으려고...
	}
	
	
	/**
	 * Json 스트링을 header 에 넣을수 있도록 encode 해주는 샘플.
	 */
	private static void testCreateHeaderMember() {
		String sessionJson = null;
		sessionJson = getSessionJson();
		sessionJson = getSessionJsonJamsilmr(); // 
		String browserHeader = Base64Utils.encodeToString(sessionJson.getBytes(StandardCharsets.UTF_8));
		log.info("testCreateHeaderMember={}", browserHeader); // 이건 헤더에 넣으려고...
	}
	

	/**
	 * 여기서는 dev 에서 로그인하고, 필요한 정보를 수정해서 header 를 만들어줄수 있도록 한다.
	 * swagger 를 이용한 테스트를 할수 있으며, 해더정보를 넣으면 관련 api 도 전파하지 않을까나?
	 * 생각보다 개발 하기에는 좋은 시스템이는한데...
	 * 
	 * @return
	 */
	private static String getSessionJsonJamsilmr() {
		// jamsilmr@naver.com
		return "{\n"
				+ "    \"cmwmUserId\" :  \"jamsilmr@naver.com\",\n"
				+ "    \"cmwmMbrNm\" :  \"박준혁\",\n"
				+ "    \"cmwmRnmCheckYn\" :  null,\n"
				+ "    \"cmwmDupScrbInfo\" :  \"MC0GCCqGSIb3DQIJAyEA6nd2T2DwylLKnReDRZwUkexDQlsjvohzsKFID8/XiDs=\",\n"
				+ "    \"cmwmCnntInfo\" :  \"tySatFuUPU0//s7jai5iaRgmHJf1mEFcjOCdTx+nR7RU9iXBNCtJX/IAoK2fQznAlVzlyI5jg+fiyqOx7J8ruA==\",\n"
				+ "    \"cmwmSsnSexCd\" :  \"1\",\n"
				+ "    \"cmwmSsnBirthDt\" :  \"970419\",\n"
				+ "    \"selSvcCnntInfo\" :  null,\n"
				+ "    \"zsvcSvcMgmtNum\" :  \"7304519837\",\n"
				+ "    \"zsvcSvcNum\" :  \"01040209292\",\n"
				+ "    \"zsvcSvcCd\" :  \"C\",\n"
				+ "    \"zsvcSvcGrCd\" :  \"SC\",\n"
				+ "    \"zsvcSvcStCd\" :  \"AC\",\n"
				+ "    \"zsvcFeeProdId\" :  \"NA00008145\",\n"
				+ "    \"twdGrCd\" :  \"A\",\n"
				+ "    \"twdMbrGrCd\" :  \"B\",\n"
				+ "    \"msvcCount\" :  \"0\",\n"
				+ "    \"twdGrType\" :  \"FM\",\n"
				+ "    \"mbrCardNum1\" :  \"2838731213\",\n"
				+ "    \"mbrCardNum2\" :  \"571029\",\n"
				+ "    \"mbrGrCd\" :  \"S\",\n"
				+ "    \"mbrSvcMgmtNum\" :  \"7304519837\",\n"
				+ "    \"mbrSvcNum\" :  \"01040209292\",\n"
				+ "    \"mbrSvcStCd\" :  \"AC\",\n"
				+ "    \"mbrDupScrbInfo\" :  \"MC0GCCqGSIb3DQIJAyEA6nd2T2DwylLKnReDRZwUkexDQlsjvohzsKFID8/XiDs=\",\n"
				+ "    \"mbrCustNm\" :  \"박준혁\",\n"
				+ "    \"mbrSsnBirthDt\" :  \"970419\",\n"
				+ "    \"mbrSsnSexCd\" :  \"1\",\n"
				+ "    \"mbrSvcGrCd\" :  \"SC\",\n"
				+ "    \"mbrStCd\" :  \"AC\",\n"
				+ "    \"mbrScrbDt\" :  \"20230905\",\n"
				+ "    \"mbrCardBenfTypCd\" :  \"01\",\n"
				+ "    \"mbrCardBenfTypChgDt\" :  \"20231025\",\n"
				+ "    \"preMbrCardNum1\" :  \"2838731213\",\n"
				+ "    \"preMbrCardNum2\" :  \"571029\",\n"
				+ "    \"preMbrGrCd\" :  \"S\",\n"
				+ "    \"preMbrSvcMgmtNum\" :  \"7304519837\",\n"
				+ "    \"preMbrSvcNum\" :  \"01040209292\",\n"
				+ "    \"preMbrSvcStCd\" :  \"AC\",\n"
				+ "    \"preMbrSvcGrCd\" :  \"SC\",\n"
				+ "    \"preMbrStCd\" :  \"AC\",\n"
				+ "    \"preMbrScrbDt\" :  \"20230905\",\n"
				+ "    \"preMbrCardBenfTypCd\" :  \"01\",\n"
				+ "    \"preMbrCardBenfTypChgDt\" :  \"20231025\",\n"
				+ "    \"multiCardExistYn\" :  null,\n"
				+ "    \"ocbCardNum\" :  null,\n"
				+ "    \"ocbCardPwd\" :  null,\n"
				+ "    \"paymentOCBCardNum\" :  null,\n"
				+ "    \"paymentOCBCardPwd\" :  null,\n"
				+ "    \"osType\" :  \"webTest\",\n"
				+ "    \"isChocoUserYn\" :  null,\n"
				+ "    \"email\" :  null,\n"
				+ "    \"emailRcvYn\" :  null,\n"
				+ "    \"svcTermYn\" :  null,\n"
				+ "    \"isUnLimitYn\" :  \"N\",\n"
				+ "    \"mbrChlId\" :  \"F4F365DE948C40CB924C68BF734DA61520190726204108\",\n"
				+ "    \"ssoSessionId\" :  null,\n"
				+ "    \"tidEmail\" :  \"jamsilmr@naver.com\",\n"
				+ "    \"nickNm\" :  \"\",\n"
				+ "    \"rcvAgreeYn\" :  \"Y\",\n"
				+ "    \"rcvAgreeDt\" :  \"20230904145025\",\n"
				+ "    \"zcstCustTypCd\" :  null,\n"
				+ "    \"emailVrfd\" :  null,\n"
				+ "    \"mdnVrfd\" :  null,\n"
				+ "    \"appVer\" :  \"testVer\",\n"
				+ "    \"mainScreenLoadedYn\" :  \"N\",\n"
				+ "    \"gnbMapList\" :  null,\n"
				+ "    \"gnbSession\" :  null,\n"
				+ "    \"component\" :  \"APP_BFF\",\n"
				+ "    \"age\" :  \"26\",\n"
				+ "    \"isAdultYn\" :  \"Y\",\n"
				+ "    \"lastLoginDate\" :  null,\n"
				+ "    \"firstLoginDate\" :  null,\n"
				+ "    \"termReqDate\" :  \"\",\n"
				+ "    \"custNum\" :  null,\n"
				+ "    \"mpCardList\" :  [\n"
				+ "        {\n"
				+ "            \"svcMgmtNum\" :  \"7304519837\",\n"
				+ "            \"svcNum\" :  \"01040209292\",\n"
				+ "            \"svcStCd\" :  \"AC\",\n"
				+ "            \"mbrCardNum1\" :  \"2838731213\",\n"
				+ "            \"mbrCardNum2\" :  \"571029\",\n"
				+ "            \"mbrGrCd\" :  \"S\",\n"
				+ "            \"mbrStCd\" :  \"AC\",\n"
				+ "            \"mbrScrbDt\" :  \"20230905\",\n"
				+ "            \"mbrTermReqDt\" :  \"\",\n"
				+ "            \"mbrCardBenfTypCd\" :  \"01\",\n"
				+ "            \"mbrCardBenfTypChgDt\" :  \"20231025\"\n"
				+ "        }\n"
				+ "    ],\n"
				+ "    \"encUserId\" :  \"ae1fffedbd057e375a39e021f13644a366fb2de115a0b82ef63668ab89f1c707\",\n"
				+ "    \"installer\" :  \"etc\",\n"
				+ "    \"mdn\" :  \"01040209292\"\n"
				+ "}";
	}
	private static String getSessionJson() {
		// 필요한 사용자 정보를 로그인 한후에 아래 내용위에 덧붙인다. 파일로 할까??
		String loginData = "{\n"
				+ "    \"cmwmUserId\" :  \"nxmania\",\n"
				+ "    \"cmwmMbrNm\" :  \"박현석\",\n"
				+ "    \"cmwmRnmCheckYn\" :  null,\n"
				+ "    \"cmwmDupScrbInfo\" :  \"MC0GCCqGSIb3DQIJAyEAls1N22lcsQITcem0zD/9syIzrU9XqzGONiA36Oy+BQc=\",\n"
				+ "    \"cmwmCnntInfo\" :  \"tjnX+NlC6T4OaLhLKaH4xT42HTk25SDXCXBnZ3LbZ+g4i3qXOXrh57QR0tb7Sr0B4LLovIMpF0lmN26Ryw1gPg==\",\n"
				+ "    \"cmwmSsnSexCd\" :  \"1\",\n"
				+ "    \"cmwmSsnBirthDt\" :  \"780328\",\n"
				+ "    \"selSvcCnntInfo\" :  null,\n"
				+ "    \"zsvcSvcMgmtNum\" :  \"7026440579\",\n"
				+ "    \"zsvcSvcNum\" :  \"01089302163\",\n"
				+ "    \"zsvcSvcCd\" :  \"C\",\n"
				+ "    \"zsvcSvcGrCd\" :  \"GF\",\n"
				+ "    \"zsvcSvcStCd\" :  \"AC\",\n"
				+ "    \"zsvcFeeProdId\" :  \"NA00007300\",\n"
				+ "    \"twdGrCd\" :  \"A\",\n"
				+ "    \"twdMbrGrCd\" :  \"A\",\n"
				+ "    \"msvcCount\" :  \"0\",\n"
				+ "    \"twdGrType\" :  \"FM\",\n"
				+ "    \"mbrCardNum1\" :  \"2469320183\",\n"
				+ "    \"mbrCardNum2\" :  \"941312\",\n"
				+ "    \"mbrGrCd\" :  \"V\",\n"
				+ "    \"mbrSvcMgmtNum\" :  \"7026440579\",\n"
				+ "    \"mbrSvcNum\" :  \"01089302163\",\n"
				+ "    \"mbrSvcStCd\" :  \"AC\",\n"
				+ "    \"mbrDupScrbInfo\" :  \"MC0GCCqGSIb3DQIJAyEAls1N22lcsQITcem0zD/9syIzrU9XqzGONiA36Oy+BQc=\",\n"
				+ "    \"mbrCustNm\" :  \"박현석\",\n"
				+ "    \"mbrSsnBirthDt\" :  \"780328\",\n"
				+ "    \"mbrSsnSexCd\" :  \"1\",\n"
				+ "    \"mbrSvcGrCd\" :  \"GF\",\n"
				+ "    \"mbrStCd\" :  \"AC\",\n"
				+ "    \"mbrScrbDt\" :  \"20151114\",\n"
				+ "    \"mbrCardBenfTypCd\" :  \"01\",\n"
				+ "    \"mbrCardBenfTypChgDt\" :  \"20231025\",\n"
				+ "    \"preMbrCardNum1\" :  \"2469320183\",\n"
				+ "    \"preMbrCardNum2\" :  \"941312\",\n"
				+ "    \"preMbrGrCd\" :  \"V\",\n"
				+ "    \"preMbrSvcMgmtNum\" :  \"7026440579\",\n"
				+ "    \"preMbrSvcNum\" :  \"01089302163\",\n"
				+ "    \"preMbrSvcStCd\" :  \"AC\",\n"
				+ "    \"preMbrSvcGrCd\" :  \"GF\",\n"
				+ "    \"preMbrStCd\" :  \"AC\",\n"
				+ "    \"preMbrScrbDt\" :  \"20151114\",\n"
				+ "    \"preMbrCardBenfTypCd\" :  \"01\",\n"
				+ "    \"preMbrCardBenfTypChgDt\" :  \"20231025\",\n"
				+ "    \"multiCardExistYn\" :  null,\n"
				+ "    \"ocbCardNum\" :  null,\n"
				+ "    \"ocbCardPwd\" :  null,\n"
				+ "    \"paymentOCBCardNum\" :  null,\n"
				+ "    \"paymentOCBCardPwd\" :  null,\n"
				+ "    \"osType\" :  \"webTest\",\n"
				+ "    \"isChocoUserYn\" :  null,\n"
				+ "    \"email\" :  null,\n"
				+ "    \"emailRcvYn\" :  null,\n"
				+ "    \"svcTermYn\" :  null,\n"
				+ "    \"isUnLimitYn\" :  \"N\",\n"
				+ "    \"mbrChlId\" :  \"73872be50fd87f92b496c4e2ed9623d620031208183828\",\n"
				+ "    \"ssoSessionId\" :  null,\n"
				+ "    \"tidEmail\" :  \"nxmania@naver.com\",\n"
				+ "    \"nickNm\" :  \"\",\n"
				+ "    \"rcvAgreeYn\" :  \"N\",\n"
				+ "    \"rcvAgreeDt\" :  \"20140115124857\",\n"
				+ "    \"zcstCustTypCd\" :  null,\n"
				+ "    \"emailVrfd\" :  null,\n"
				+ "    \"mdnVrfd\" :  null,\n"
				+ "    \"appVer\" :  \"testVer\",\n"
				+ "    \"mainScreenLoadedYn\" :  \"N\",\n"
				+ "    \"gnbMapList\" :  null,\n"
				+ "    \"gnbSession\" :  null,\n"
				+ "    \"component\" :  \"APP_BFF\",\n"
				+ "    \"age\" :  \"45\",\n"
				+ "    \"isAdultYn\" :  \"Y\",\n"
				+ "    \"lastLoginDate\" :  null,\n"
				+ "    \"firstLoginDate\" :  null,\n"
				+ "    \"termReqDate\" :  \"\",\n"
				+ "    \"custNum\" :  null,\n"
				+ "    \"mpCardList\" :  [\n"
				+ "        {\n"
				+ "            \"svcMgmtNum\" :  \"7026440579\",\n"
				+ "            \"svcNum\" :  \"01089302163\",\n"
				+ "            \"svcStCd\" :  \"AC\",\n"
				+ "            \"mbrCardNum1\" :  \"2469320183\",\n"
				+ "            \"mbrCardNum2\" :  \"941312\",\n"
				+ "            \"mbrGrCd\" :  \"V\",\n"
				+ "            \"mbrStCd\" :  \"AC\",\n"
				+ "            \"mbrScrbDt\" :  \"20151114\",\n"
				+ "            \"mbrTermReqDt\" :  \"\",\n"
				+ "            \"mbrCardBenfTypCd\" :  \"01\",\n"
				+ "            \"mbrCardBenfTypChgDt\" :  \"20231025\"\n"
				+ "        }\n"
				+ "    ],\n"
				+ "    \"encUserId\" :  \"f600d25f53d58503c40f49d10082ac08\",\n"
				+ "    \"installer\" :  \"etc\",\n"
				+ "    \"mdn\" :  \"01089302163\"\n"
				+ "}";
		return loginData;
	}
}
