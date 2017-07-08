package wuxian.me.lagouspider.model.lagou;

import wuxian.me.lagouspider.biz.boss.BossConfig;
import wuxian.me.lagouspider.model.BaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static wuxian.me.lagouspider.biz.lagou.BaseLagouSpider.*;


/**
 * Created by wuxian on 30/3/2017.
 */
public class Position extends BaseModel {

    public static String tableName = BossConfig.TableName.POSITION;

    public Integer salaryMin;

    public Integer salaryMax;

    public Integer experienceMin;

    public Integer experienceMax;

    public Integer sizeMin;

    public Integer sizeMax;

    //发布时间
    public String postTime;

    public Long companyId;

    public String companyShortName;

    public String district;

    public String education;

    public Long positionId;

    public String positionName;

    public String industryField;

    //是否是全职
    public Boolean fullTime;

    public String financeStage;

    public String positionLables;

    private static String dateformat = "yyyy-MM-dd";
    private static SimpleDateFormat sdf = new SimpleDateFormat(dateformat);

    public boolean outOfDate() {
        try {
            Date date = sdf.parse(postTime);

            return new Date().getDay() - date.getDay() >= 7;

        } catch (ParseException e) {
            return false;
        }
    }

    public String name() {
        return "Position{" +
                "salaryMin=" + salaryMin +
                ", salaryMax=" + salaryMax +
                ", experienceMin=" + experienceMin +
                ", experienceMax=" + experienceMax +
                ", sizeMin=" + sizeMin +
                ", sizeMax=" + sizeMax +
                ", postTime='" + postTime + '\'' +
                ", companyId=" + companyId +
                ", companyShortName='" + companyShortName + '\'' +
                ", district='" + district + '\'' +
                ", education='" + education + '\'' +
                ", positionId=" + positionId +
                ", positionName='" + positionName + '\'' +
                ", industryField='" + industryField + '\'' +
                ", fullTime=" + fullTime +
                ", financeStage='" + financeStage + '\'' +
                ", positionLables='" + positionLables + '\'' +
                '}';
    }

    @Override
    public long index() {
        return positionId;
    }

    public static Position fromLPosition(LPosition lPosition) {

        Position position = new Position();
        position.salaryMin = getMatchedGroupInteger(SALARY_MIN_PATTERN, lPosition.salary);

        position.salaryMax = getMatchedGroupInteger(SALARY_MAX_PATTERN, lPosition.salary);

        position.experienceMin = getMatchedGroupInteger(WORK_MIN_PATTERN, lPosition.workYear);

        position.experienceMax = getMatchedGroupInteger(WORK_MAX_PATTERN, lPosition.workYear);

        if (position.experienceMin == null && position.experienceMax == null) {
            position.experienceMin = -1;
            position.experienceMax = -1;
        }

        if (lPosition.companySize.contains("以上")) {
            position.sizeMin = getMatchedGroupInteger(COMPANY_PATTERN, lPosition.companySize);
        } else {
            position.sizeMin = getMatchedGroupInteger(COMPANY_MIN_PATTERN, lPosition.companySize);
            position.sizeMax = getMatchedGroupInteger(COMPANY_MAX_PATTERN, lPosition.companySize);
        }

        position.postTime = getMatchedGroup(CREATE_PATTERN, lPosition.createTime);

        position.companyId = lPosition.companyId;

        position.companyShortName = lPosition.companyShortName;

        position.district = lPosition.district;

        position.education = lPosition.education;

        position.positionId = lPosition.positionId;

        position.positionName = lPosition.positionName;

        position.industryField = lPosition.industryField;

        position.fullTime = lPosition.jobNature.equals("全职");

        position.financeStage = lPosition.financeStage;

        if (lPosition.positionLables == null || lPosition.positionLables.size() == 0) {
            position.positionLables = null;
        } else {
            StringBuilder builder = new StringBuilder("");
            for (String b : lPosition.positionLables) {
                builder.append(b + ",");
            }
            position.positionLables = builder.toString();
        }

        return position;
    }

    private static final String REG_CREATE = "[0-9-]+";
    private static final Pattern CREATE_PATTERN = Pattern.compile(REG_CREATE);

    private static final String REG_COMPANY = "[0-9]+";
    private static final Pattern COMPANY_PATTERN = Pattern.compile(REG_COMPANY);

    private static final String REG_COMPANY_MIN = "[0-9]+(?=-)";
    private static final Pattern COMPANY_MIN_PATTERN = Pattern.compile(REG_COMPANY_MIN);

    private static final String REG_COMPANY_MAX = "(?<=-)[0-9]+";
    private static final Pattern COMPANY_MAX_PATTERN = Pattern.compile(REG_COMPANY_MAX);

    private static final String REG_SALARY_MIN = "[0-9]+(?=[kK]-)";
    private static final Pattern SALARY_MIN_PATTERN = Pattern.compile(REG_SALARY_MIN);

    private static final String REG_SALARY_MAX = "(?<=-)[0-9]+";
    private static final Pattern SALARY_MAX_PATTERN = Pattern.compile(REG_SALARY_MAX);

    private static final String REG_WORK_MIN = "[0-9]+";
    private static final Pattern WORK_MIN_PATTERN = Pattern.compile(REG_WORK_MIN);

    private static final String REG_WORK_MAX = "(?<=-)[0-9]+";
    private static final Pattern WORK_MAX_PATTERN = Pattern.compile(REG_WORK_MAX);

    private static final String REG_PRICE = "[0-9]+(?=元/m2)";
    private static final Pattern PRICE_PATTERN = Pattern.compile(REG_PRICE);


}
