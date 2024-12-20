package com.bjcareer.GPTService.domain.helper;

public class LevenshteinDistance {

	public static int calculateLevenshteinDistance(String str1, String str2) {
		//비교하지 않는 방식
		if(str1.length() <= 3){
			return 99999;
		}

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
}
