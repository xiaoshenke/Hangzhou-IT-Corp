package wuxian.me.lagoujob.util;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagoujob.model.lagou.Company;

/**
 * Created by wuxian on 7/5/2017.
 */
public class ScoreUtil {
    private ScoreUtil() {
    }

    //赋值company的一些域比如@financeScore,@authenScore
    public static void calScoreFieldOf(Company company) {
        int scores = 0;
        int score = 0;

        company.stage = FinanceUtil.from(company).getValue();

        score = calFinanceScore(company);
        company.finaceScore = score;
        scores += score;

        score = calLagouAuthenScore(company);
        company.authenScore = score;
        scores += score;

        score = calJobNum(company);
        company.positionNumScore = score;
        scores += score;

        score = calInterviewScore(company);
        company.interScore = score;
        scores += score;

        company.score = scores;
        return;
    }

    //对于不同的公司 需求是不一样的 比如说大公司有很大的需求 那么说明这个公司有新业务
    //如果是个天使的公司 都没招什么人 估计就是死了
    private static int calJobNum(Company company) {
        int position = format(company.positionNum);
        int stage = FinanceUtil.from(company).getValue();

        if (stage == Finance.STAGE_TIANSHI.getValue()) {
            if (position < 5) {
                return -5;
            } else if (position >= 10) {  //正在快速扩张
                return (position / 10) * 10;
            }

            return 0;
        }

        if (stage == Finance.STAGE_A.getValue()) {
            if (position <= 5) {  //应该扩张的时候没招人
                return -10;
            } else if (position >= 20) {
                return ((position - 20) / 10 + 1) * 10;
            }

            return 0;
        }

        if (stage >= Finance.STAGE_B.getValue() && stage <= Finance.STAGE_C.getValue()) {
            if (position <= 10) {
                return -15;
            } else if (position >= 40 && position <= 160) {
                return ((position - 40) / 40 + 1) * 10;
            } else if (position > 160) {
                return 40;
            }

            return 0;
        }

        if (stage >= Finance.STAGE_D_OR_PLUS.getValue()) {
            if (position <= 20) {
                return -20;
            } else if (position >= 80 && position <= 240) {
                return ((position - 40) / 40 + 1) * 10;
            } else if (position > 200) {
                return 50;
            }

            return 0;
        }

        if (stage >= Finance.STAGE_NONE.getValue() && stage <= Finance.STAGE_NONEED.getValue()) {
            if (position <= 5) {
                return -10;
            } else if (position >= 40 && position <= 120) {
                return ((position - 40) / 20 + 1) * 5;
            } else if (position > 120) {
                return 25;
            }
            return 0;
        }

        return 0;
    }

    private static int calFinanceScore(Company company) {
        return FinanceUtil.from(company).getValue() * 10;
    }

    private static int calLagouAuthenScore(Company company) {
        if (company.lagouAuthentic != null && company.lagouAuthentic.equals("true")) {
            return 20;
        }

        return 0;
    }

    //基本规律：轮次越高的公司拿到高分的概率越低
    // 但是若轮次很高的公司 能够拿到很高的分数 那说明这家单位很靠谱 --> 不一定 由于大公司光环 它们的分数普遍较高
    //迷之不需要融资和未融资的公司 这里统一返回0
    private static int calInterviewScore(Company company) {
        if (company.interviewScore == null) {
            return 0;
        }

        int stage = FinanceUtil.from(company).getValue();
        float interview = Float.parseFloat(company.interviewScore);

        if (stage <= Finance.STAGE_A.getValue()) {
            if (interview >= 4.5 && interview <= 4.8) {
                return 20;
            } else if (interview >= 4.2 && interview < 4.5) {
                return 10;
            } else if (interview > 4.8) {  //这么高分 太假了
                return 10;
            }

            if (interview >= 3.0 && interview <= 3.5) {
                return -5;
            } else if (interview >= 2.0 && interview < 3.0) {
                return -10;
            } else if (interview >= 1.0 && interview < 2.0) {
                return -20;
            } else if (interview < 1.0) {
                return -30;
            }

            return 0;
        }

        //基本上4.5以上就不会出现b c轮的公司了
        if (stage >= Finance.STAGE_B.getValue() && stage <= Finance.STAGE_C.getValue()) {
            int base = 15;
            if (interview >= 4.3) {
                return 20;
            } else if (interview >= 4.0 && interview < 4.5) {
                return 10;
            }

            if (interview >= 2.8 && interview <= 3.2) {
                return -10;
            } else if (interview >= 2.0 && interview < 2.8) {
                return -20;
            } else if (interview >= 1.0 && interview < 2.0) {
                return -30;
            } else if (interview < 1.0) {
                return -40;
            }

            return 0;
        }


        if (stage >= Finance.STAGE_D_OR_PLUS.getValue()) {
            if (interview >= 3.9 && interview <= 4.3) {
                return 20;
            } else if (interview >= 3.5 && interview < 3.9) {
                return 10;
            } else if (interview > 4.3) {  //这么高分 太假了
                return 10;
            }

            if (interview >= 2.3 && interview <= 2.8) {
                return -20;
            } else if (interview >= 1.5 && interview < 2.3) {
                return -30;
            } else if (interview < 1.5) {
                return -40;
            }

            return 0;
        }
        return 0;
    }

    private static int format(@NotNull String num) {
        int index = num.indexOf("个");
        if (index != -1) {
            return Integer.parseInt(num.substring(0, index));
        } else {
            return Integer.parseInt(num);
        }
    }
}
