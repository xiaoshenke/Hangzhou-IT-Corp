package wuxian.me.lagouspider.model;

/**
 * Created by wuxian on 30/3/2017.
 *
 * Fixme:如何处理有多个地址的公司 --> 多一张表记录多个地址的公司？这个数据量不会很大
 */
public class Company {

    public int area_id = -1;
    public String company_fullname;
    public String financeStage;
    public String industryField;
    public String detail_location;

    public Company(long companyId) {
        this.company_id = companyId;
    }

    public long company_id;  //--> https://www.lagou.com/gongsi/110890.html

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
 * "adWord":0,
 * "createTime":"2017-03-30 10:02:39",
 * "companyShortName":"浙江小融网络科技股份有限公司",
 * "positionId":2842448,
 * "score":0,
 * "positionAdvantage":"五险一金,周末双休",
 * "salary":"15k-18k",
 * "workYear":"3-5年",
 * "education":"大专",
 * "city":"杭州",
 * "positionName":"中级java工程师",
 * "companyLogo":"i/image/M00/00/31/Cgp3O1YxznqAF4NhAAAR4jC1h5E922.jpg",
 * "financeStage":"成长型(A轮)",
 * "industryField":"金融",
 * "jobNature":"全职",
 * "companySize":"50-150人",
 * "approve":1,
 * "district":"西湖区",
 * "companyLabelList":["年底双薪","绩效奖金","年度旅游","岗位晋升"],
 * "companyFullName":"浙江小融网络科技股份有限公司",
 * "plus":null,
 * "pcShow":0,
 * "appShow":0,
 * "deliver":0,
 * "formatCreateTime":"2天前发布",
 * "gradeDescription":null,
 * "promotionScoreExplain":null,
 * "firstType":"开发/测试/运维类",
 * "secondType":"后端开发",
 * "positionLables":["中级","Java"],
 * "businessZones":["西溪","古墩路","古荡"],
 * "imState":"today",
 * "lastLogin":1491015825000,
 * "publisherId":1306735,
 * "explain":null
 **/