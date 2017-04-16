package wuxian.me.lagouspider.model;

/**
 * Created by wuxian on 30/3/2017.
 *
 * 处理有多个地址的公司 --> 多一张表记录多个地址的公司
 */
public class Company {
    public static String tableName;

    public int area_id = -1;
    public long company_id;

    public String company_fullname;  //这些可以从@AreaPageSpider拿
    public String industryField;
    public String company_size;

    public String logo;   //这些需要从@CompanySpider拿
    public String detail_location;
    public String financeStage;
    public String webLink;
    public String lagouAuthentic;
    public String description;
    public String positionNum;
    public String resumeRate;
    public String interviewNum;

    public String score;       //面试评价评分
    public String accordSore;  //描述是否相符
    public String interviewerScore;
    public String environmentScore;

    //public String labelList; //lagou给的labelList 比如什么五险一金 年底双薪啦

    public Company(long companyId) {
        this.company_id = companyId;
    }

    @Override
    public int hashCode() {
        return (int) company_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Company) {
            return company_id == ((Company) obj).company_id;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Company: {id: " + company_id + " name: " + company_fullname + " stage: " + financeStage + " industry: " + industryField + "}";
    }
}


/**
 * "company_id":51881,
 * "companyShortName":"浙江小融网络科技股份有限公司",
 * "companyLogo":"i/image/M00/00/31/Cgp3O1YxznqAF4NhAAAR4jC1h5E922.jpg",
 * "financeStage":"成长型(A轮)",
 * "industryField":"金融",
 * "companySize":"50-150人",
 * "companyLabelList":["年底双薪","绩效奖金","年度旅游","岗位晋升"],
 * "companyFullName":"浙江小融网络科技股份有限公司",
 * "businessZones":["西溪","古墩路","古荡"],
 **/