package wuxian.me.lagouspider.model.lagou;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 7/7/2017.
 */
public class LPosition {

    public Long companyId;

    public String companyShortName;

    public String district;

    public String education;

    public String createTime;

    public Long positionId;

    public String positionName;

    public String salary;

    public String workYear;

    public String industryField;

    public String jobNature;

    public String financeStage;

    public String companySize;

    public List<String> positionLables = new ArrayList<String>();

    @Override
    public String toString() {
        return "LPosition{" +
                "companyId=" + companyId +
                ", companyShortName='" + companyShortName + '\'' +
                ", district='" + district + '\'' +
                ", education='" + education + '\'' +
                ", createTime='" + createTime + '\'' +
                ", positionId=" + positionId +
                ", positionName='" + positionName + '\'' +
                ", salary='" + salary + '\'' +
                ", workYear='" + workYear + '\'' +
                ", industryField='" + industryField + '\'' +
                ", jobNature='" + jobNature + '\'' +
                ", financeStage='" + financeStage + '\'' +
                ", companySize='" + companySize + '\'' +
                ", positionLables=" + positionLables +
                '}';
    }
}
