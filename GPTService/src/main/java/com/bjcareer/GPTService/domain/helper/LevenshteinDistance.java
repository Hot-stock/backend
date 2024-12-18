package com.bjcareer.GPTService.domain.helper;

public class LevenshteinDistance {

	public static int calculateLevenshteinDistance(String str1, String str2) {
		//비교하지 않는 방식
		if(str1.length() <= 3){
			return 10;
		}

		str1 = splitHangul(str1);
		str2 = splitHangul(str2);

		int lenStr1 = str1.length();
		int lenStr2 = str2.length();

		// 2D 배열 초기화
		int[][] dp = new int[lenStr1 + 1][lenStr2 + 1];

		// 초기 값 설정 (빈 문자열과의 거리)
		for (int i = 0; i <= lenStr1; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= lenStr2; j++) {
			dp[0][j] = j;
		}

		// 배열 채우기
		for (int i = 1; i <= lenStr1; i++) {
			for (int j = 1; j <= lenStr2; j++) {
				// 문자 비교
				int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;

				// 삽입, 삭제, 교체 중 최소값 선택
				dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, // 삭제
						dp[i][j - 1] + 1), // 삽입
					dp[i - 1][j - 1] + cost); // 교체
			}
		}

		return dp[lenStr1][lenStr2];
	}

	public static String splitHangul(String word) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			char syllable = word.charAt(i);
			if (syllable >= 0xAC00 && syllable <= 0xD7A3) {
				int unicode = syllable - 0xAC00;
				int cho = unicode / (21 * 28);
				int jung = (unicode % (21 * 28)) / 28;
				int jong = unicode % 28;

				// 초성, 중성, 종성 자모 추출
				result.append((char)(cho + 0x1100))   // 초성
					.append((char)(jung + 0x1161))  // 중성
					.append(jong != 0 ? (char)(jong + 0x11A7) : ""); // 종성 (없으면 빈 문자열)
			} else {
				result.append(syllable);
			}
		}
		return result.toString();
	}
}
