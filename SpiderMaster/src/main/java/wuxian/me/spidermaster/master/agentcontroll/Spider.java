package wuxian.me.spidermaster.master.agentcontroll;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 27/5/2017.
 *
 * Spider具有的爬虫的能力
 * 能力应该包含spider的class名字,该class对应的url pattern。
 *
 * Todo:监控成功的任务数,失败的任务数
 *
 */
public class Spider {

    private List<Feature> featureList = new ArrayList<Feature>();

    public void addFeature(Feature feature) {
        if (feature == null) {
            return;
        }

        featureList.add(feature);
    }

    public static class Feature {

        public String className;

        public String urlPattern;
    }
}
