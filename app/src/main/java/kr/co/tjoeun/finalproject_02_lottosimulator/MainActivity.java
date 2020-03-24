package kr.co.tjoeun.finalproject_02_lottosimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.tjoeun.finalproject_02_lottosimulator.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    List<TextView> winNumTxtList = new ArrayList<>();
    int[] winLottoNumArr = new int[6];
    int bonusNum = 0;


    int[] myLottoNumArr = {12,15,30,26,14,19,33};
    long winMoneyAmount = 0;
    long userMoneyAmount = 0;

    int firstRankCount = 0;
    int secondRankCount = 0;
    int thirdRankCount = 0;
    int forthRankCount = 0;
    int fifthRankCount = 0;
    int noRankCount = 0;


    ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        binding.buyAutoLottoBtn.setOnClickListener(new View.OnClickListener() {

//            사용 금액의 총액이 1천만원이 될때 까지 반복.

            @Override
            public void onClick(View v) {

                while (userMoneyAmount < 10000000) {
//                     당첨번호를 만들고 등수를 카운팅에 반영
                    makeWinLottoNum();
                    checkLottoRank();
                }

//                당첨번호를 생성 => 텍스트뷰에 반영
                makeWinLottoNum();
//                몇등인지 판단
                checkLottoRank();
            }
        });

    }

    @Override
    public void setValues() {

        winNumTxtList.add(binding.winLottoNumtxt01);
        winNumTxtList.add(binding.winLottoNumtxt02);
        winNumTxtList.add(binding.winLottoNumtxt03);
        winNumTxtList.add(binding.winLottoNumtxt04);
        winNumTxtList.add(binding.winLottoNumtxt05);
        winNumTxtList.add(binding.winLottoNumtxt06);

    }

    void makeWinLottoNum() {

//        6개의 숫자(배열) + 보너스번호 1개 int변수
//        => 이 함수에서만이 아니라, 다른곳에서도 쓸 예정.
//        => 당첨 등수 확인때도 사용. => 멤버변수로 배열 / 변수 생성

//        당첨번호+보너스번호를 모두 0으로 초기화.
//         (이미 뽑은 번호가 있다면 모두 날리자)

        for (int i=0 ; i < winLottoNumArr.length ; i++) {
            winLottoNumArr[i] = 0;
        }
        bonusNum = 0;

//        로또번호 6개 생성.
//        1~45여야 함 + 중복 허용 X.

        for (int i=0 ; i <winLottoNumArr.length ; i++) {
//            1~45의 숫자를 랜덤으로 뽑고
//            중복이 아니라면 => 당첨번호로 선정.
//            중복이라면? => 다시 뽑자. => 중복이 아닐때까지 계속 뽑자.

            while (true) {

//                1 <= (int) (Math.random() * 45 + 1) < 46

//                1~45의 정수를 랜덤으로 뽑아서 임시저장.
//                이 숫자가 중복검사를 통과하면 사용, 아니면 다시.
                int randomNum = (int) (Math.random() * 45 + 1);

//                중복검사? 당첨번호 전부와 randomNum을 비교.
//                하나라도 같으면 탈락.

                boolean isDuplOk = true; // 중복검사 변수
                for (int winNum : winLottoNumArr) {
                    if (winNum == randomNum) {
                        isDuplOk = false; //탈락!
                        break;
                    }
                }

                if (isDuplOk) {
                    winLottoNumArr[i] = randomNum;
                    Log.i("당첨번호", randomNum+"");
                    break; // 무한반복 탈출
                }

            }

        }
        Arrays.sort(winLottoNumArr);

        for (int i = 0; i < winLottoNumArr.length; i++) {

            winNumTxtList.get(i).setText(winLottoNumArr[i]+"");

        }

//        보너스 번호 생성 => 1~45, 당첨 번호 중복X.

        while (true) {
            int randomNum = (int) (Math.random()*45 +1);

            boolean isDuplOk = true;
            for (int winNum : winLottoNumArr) {
                if (winNum == randomNum) {
                    isDuplOk = false;
                    break;
                }
            }

            if (isDuplOk) {
                bonusNum = randomNum;
                break;
            }

        }
//        보너스 생성됨
        binding.winLottoBonusNumtxt.setText(bonusNum+"");


    }

    void checkLottoRank() {
//        등수 확인 + 돈 천원 지불.
        userMoneyAmount += 1000;

        binding.useMoneyTxt.setText(String.format("사용 금액 : +%,d원", userMoneyAmount));

        int correctCount = 0;

        for (int myNum : myLottoNumArr) {
            for (int winNum : winLottoNumArr) {
                if (myNum == winNum) {
                    correctCount++;
                }

            }
        }

//        correctCount의 값에따라 등수를 판정.
        if (correctCount == 6) {
//            1등 12억
            winMoneyAmount += 1600000000;
            fifthRankCount ++;
        }
        else if (correctCount == 5) {
//            2등/3등 재검사. => 보너스 번호가 맞는지?
//            => 내 번호중에 보너스 번화 같은게 있나?
            boolean hasBonusNum = false;

            for (int myNum : myLottoNumArr) {
                if (myNum == bonusNum) {
                    hasBonusNum = true;
                    break;
                }
            }
            if (hasBonusNum){
//                2등
                winMoneyAmount += 75000000;
                secondRankCount ++;
            }
            else {
//                3등
                winMoneyAmount += 1500000;
                thirdRankCount ++;
            }
        }
        else if (correctCount ==4) {
            winMoneyAmount += 50000;
            forthRankCount ++;
//            4등
        }
        else if (correctCount == 3) {
//            5등
            userMoneyAmount -= 5000;
            fifthRankCount ++;
        }
        else {
//            꽝
            noRankCount ++;
        }
//        당첨금액 텍스트에도 반영
        binding.winMoneyTxt.setText(String.format("당첨금액 : %,d원",winMoneyAmount));

        binding.fifthRankCountTxt.setText(String.format("1등 : %d회", fifthRankCount));
        binding.secondRankCountTxt.setText(String.format("2등 : %d회", secondRankCount));
        binding.thirdRankCountTxt.setText(String.format("3등 : %d회", thirdRankCount));
        binding.forthRankCountTxt.setText(String.format("4등 : %d회", forthRankCount));
        binding.fifthRankCountTxt.setText(String.format("5등 : %d회", fifthRankCount));
        binding.noRankCountTxt.setText(String.format("꽝 : %d회", noRankCount));

        }

        }
