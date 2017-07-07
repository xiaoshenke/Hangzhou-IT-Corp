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

    public String formatCreateTime;

    public Long positionId;

    public String positionName;

    public String salary;

    public String workYear;

    public String industryField;

    public String jobNature;

    @Override
    public String toString() {
        return "LPosition{" +
                "companyId=" + companyId +
                ", companyShortName='" + companyShortName + '\'' +
                ", district='" + district + '\'' +
                ", education='" + education + '\'' +
                ", formatCreateTime='" + formatCreateTime + '\'' +
                ", positionId=" + positionId +
                ", positionName='" + positionName + '\'' +
                ", salary='" + salary + '\'' +
                ", workYear='" + workYear + '\'' +
                ", industryField='" + industryField + '\'' +
                ", jobNature='" + jobNature + '\'' +
                ", financeStage='" + financeStage + '\'' +
                ", positionLables=" + positionLables +
                '}';
    }

    public String financeStage;

    public List<String> positionLables = new ArrayList<String>();

}
